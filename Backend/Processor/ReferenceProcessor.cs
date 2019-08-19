using System;
using System.Diagnostics;
using System.Linq;
using System.Text.RegularExpressions;
using System.Threading;
using DataStructures;
namespace Processor {
public class ReferenceProcessor {
	private static readonly Regex Enumeration = new Regex(" (\\d+\\w?)(\\ |\\,|\\.)");
	private static readonly Regex AlphaNumEnd = new Regex("(\\d+\\w?)(\\ |\\,|\\.)");
	private static readonly Regex ArtikelN = new Regex(" (?:Artikel\\ |Art\\.\\ |§\\ )(\\d+\\w?)(\\ |\\,|\\.)");
	private static readonly Regex UndAlphaNum = new Regex(" und (\\d+\\w?)(\\ |\\,|\\.)");
	private static readonly Regex AbsAlphaNum = new Regex("(?:Absatz||Abs.)\\ (\\d+\\w?)(\\ |\\,|\\.)");

	public static void ReferenceDetector() {

foreach ((LawRef , string ) processingTuple in Program.toProcess) {
	HandleProcessItem(processingTuple);
}
	}

	public static void MCReferenceDetector() {
		var threads = new Thread[Environment.ProcessorCount];
		for (var i = 0; i < Environment.ProcessorCount; i++) {
			threads[i] = new Thread(() => {
				while (Program.toProcess.TryTake(out (LawRef, string) processTuple)) {
					HandleProcessItem(processTuple);
				}
			});threads[i].Start();
		}
Console.WriteLine($"Starting actual processing of references at {DateTime.Now}");
foreach (Thread thread in threads) {
			thread.Join();
		}

		Console.WriteLine($"Joined all threads after at {DateTime.Now}");
	}

	private static void HandleProcessItem((LawRef, string) processingTuple) {
		//Thread.Sleep(1);
		foreach (Match match in ArtikelN.Matches(processingTuple.Item2)) {
			string following = processingTuple.Item2.Substring(match.Index + match.Length);
			string paragraph = match.Groups[1].Value;

			HandleArtikelNMatch(following, processingTuple.Item1, paragraph, match.Groups[2].Value == ",");
		}
	}

	static void HandleArtikelNMatch(string following, LawRef lawDefinedIn, string paragraph, bool cont) {
		Match absMatch = AbsAlphaNum.Match(following);
		if (absMatch.Success && absMatch.Index == 0) {
			following = following.Substring(absMatch.Length);
			AddReference(lawDefinedIn,
				new LawRef {
					shorthand = lawDefinedIn.shorthand,
					paragraph = paragraph, subparagraph = absMatch.Groups[1].Value
				});
			if (absMatch.Groups[2].Value == ",") {
				Match enumerationMatch = Enumeration.Match(following);
				if (enumerationMatch.Success && enumerationMatch.Index == 0) {
					following = following.Substring(enumerationMatch.Length);
					HandleArtikelNMatch(following, lawDefinedIn, paragraph, enumerationMatch.Groups[2].Value == ",");
					return;
				}
			}

			Match abs2Match = UndAlphaNum.Match(following, 0);
			if (abs2Match.Success || abs2Match.Index == 0) {
				AddReference(lawDefinedIn,
					new LawRef {
						shorthand = lawDefinedIn.shorthand,
						paragraph = paragraph, subparagraph = abs2Match.Groups[1].Value
					});
			}
		}
		else {
			AddReference(lawDefinedIn,
				new LawRef {
					shorthand = lawDefinedIn.shorthand,
					paragraph = paragraph
				});
			if (cont && following.Length > 1) {
				following = following.Substring(1);
				Match nextArtMatch = AlphaNumEnd.Match(following);
				HandleArtikelNMatch(following.Substring(nextArtMatch.Index + nextArtMatch.Length), lawDefinedIn,
					nextArtMatch.Groups[1].Value, nextArtMatch.Groups[2].Value == ",");
			}
		}
	}

	private static void AddReference(LawRef references, LawRef referenced) {
		Paragraph firstPar = Program.root.statues.FirstOrDefault(x => referenced.shorthand == x.shorthand)?.paragraphs
			.FirstOrDefault(x => x.number == referenced.paragraph);
		if (firstPar is null) {
			return;
		}

		if (referenced.subparagraph is null && firstPar.hasSubparagraphs) {
			foreach (Subparagraph subparagraph in firstPar.subparagraphs) {
				subparagraph.AddRequired(references);
			}
		}
		else if (!firstPar.hasSubparagraphs) {
			firstPar.subparagraphs.First().AddRequired(references);
		}
		else {
			Subparagraph sb = firstPar.subparagraphs.FirstOrDefault(x => x.number == referenced.subparagraph);
			if (sb is null) {
				return;
			}

			sb.AddRequired(references);
		}

		Program.refcnt++;
	}
}
}