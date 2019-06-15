using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Xml.Linq;
using DataStructures;
using Newtonsoft.Json;

namespace Processor {
public static class Program {
	public static List<(LawRef, string)> toProcess = new List<(LawRef, string)>();
	public static JsonRoot root;
	private static int refcnt;

	static void Main(string[] args) {
		Console.WriteLine("Hello World!");
		root = new JsonRoot();
		foreach (string file in Directory.GetFiles(Downloader.Download.LawPath)) {
			XDocument currentFile = XDocument.Load(file);
			MetadataProcessor.LoadMetaData(currentFile);
		}

		ReferenceDetector();

		File.WriteAllText("root.json", JsonConvert.SerializeObject(root));
		Console.WriteLine(
			$"Found {root.statues.Sum(x => x.paragraphs.Count)} paragraphs {root.statues.Sum(x => x.paragraphs.Sum(y => y.subparagraphs.Count))} subpars and {toProcess.Count} textblocks and {refcnt} references, the json is {new FileInfo("root.json").Length} byte big");
	}

	private static void ReferenceDetector() {
		Regex alphanum= new Regex("(\\d+\\w?)");
		Regex artikeln = new Regex("Artikel (\\d+\\w?)(\\ |\\,|\\.)");
		foreach ((LawRef, string) searchTuple in toProcess) {
			foreach (Match match in artikeln.Matches(searchTuple.Item2)) {
				string following = searchTuple.Item2.Substring(match.Index + match.Length);
				string paragraph = match.Groups[1].Value;
				
				HandleArtikelnMatch(following, searchTuple, paragraph);
			}
		}

		void HandleArtikelnMatch(string following, (LawRef, string) searchTuple, string paragraph) {
			if (following.StartsWith("Absatz") || following.StartsWith("Abs.")) {
				following = following.Substring(following.IndexOf(' ') + 1);
				Match absMatch = alphanum.Match(following);
				if (!absMatch.Success||absMatch.Index!=0) {
					AddReference(searchTuple.Item1,
						new LawRef {
							shorthand = searchTuple.Item1.shorthand,
							paragraph = paragraph
						});
					return;
				}
				string abs = following.Substring(0, following.IndexOfAny(new char[] {' ', '.'}));
				AddReference(searchTuple.Item1,
					new LawRef {
						shorthand = searchTuple.Item1.shorthand,
						paragraph = paragraph, subparagraph = abs
					});
				if (following[following.IndexOfAny(new char[] {' ', '.'})] == ' ') {
					following = following.Substring(following.IndexOf(' ') + 1);
					if (following.StartsWith("und")) {
						string abs2 = following.Substring(0, following.IndexOfAny(new char[] {' ', '.'}));
						AddReference(searchTuple.Item1,
							new LawRef {
								shorthand = searchTuple.Item1.shorthand,
								paragraph = paragraph, subparagraph = abs2
							});
					}
				}
			}
			else {
				AddReference(searchTuple.Item1,
					new LawRef {
						shorthand = searchTuple.Item1.shorthand,
						paragraph = paragraph
					});
			}
		}
	}

	static bool AddReference(LawRef references, LawRef referenced) {
		Paragraph firstPar = root.statues.FirstOrDefault(x => referenced.shorthand == x.shorthand)?.paragraphs
			.FirstOrDefault(x => x.number == referenced.paragraph);
		if (firstPar is null) {
			return false;
		}

		if (referenced.subparagraph is null && firstPar.hasSubparagraphs) {
			foreach (Subparagraph subparagraph in firstPar.subparagraphs) {
				subparagraph.requiredby.Add(references);
			}
		}
		else if (!firstPar.hasSubparagraphs) {
			firstPar.subparagraphs.First().requiredby.Add(references);
		}
		else {
			Subparagraph sb = firstPar.subparagraphs.FirstOrDefault(x => x.number == referenced.subparagraph);
			if (sb is null) {
				return false;
			}

			sb.requiredby.Add(references);
		}

		refcnt++;
		return true;
	}
}
}