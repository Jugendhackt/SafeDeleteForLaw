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

		Console.WriteLine(
			$"Found {root.statues.Sum(x => x.paragraphs.Sum(y => y.subparagraphs.Count))} subpars and {toProcess.Count} textblocks and {refcnt} references");
		File.WriteAllText("root.json", JsonConvert.SerializeObject(root));
	}

	private static void ReferenceDetector() {
		Regex artikeln = new Regex("Artikel (\\d+) ");
		foreach ((LawRef, string) searchTuple in toProcess) {
			foreach (Match match in artikeln.Matches(searchTuple.Item2)) {
				string following = searchTuple.Item2.Substring(match.Index + match.Length);
				if (following.StartsWith("Absatz") || following.StartsWith("Abs.")) {
					following = following.Substring(following.IndexOf(' ') + 1);
					string abs = following.Substring(0, following.IndexOfAny(new char[] {' ', '.'}, 0));
					AddReference(searchTuple.Item1,
						new LawRef {
							shorthand = searchTuple.Item1.shorthand,
							paragraph = (match.Groups.First(x => x.GetType() == typeof(Group))).Value, subparagraph = abs
						});
					AddReference(searchTuple.Item1,
						new LawRef {
							shorthand = searchTuple.Item1.shorthand,
							paragraph = (match.Groups.First(x => x.GetType() == typeof(Group))).Value
						});
				}
				else {
					AddReference(searchTuple.Item1,
						new LawRef {
							shorthand = searchTuple.Item1.shorthand,
							paragraph = (match.Groups.First(x => x.GetType() == typeof(Group))).Value
						});
				}
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