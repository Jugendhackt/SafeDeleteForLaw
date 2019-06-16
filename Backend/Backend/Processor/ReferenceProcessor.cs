using System.Linq;
using System.Text.RegularExpressions;
using DataStructures;

namespace Processor {
public class ReferenceProcessor {
	private static readonly Regex AlphaNum = new Regex("(\\d+\\w?)");
	private static readonly Regex AlphaNumEnd = new Regex("(\\d+\\w?)(\\ |\\,|\\.)");
	private static readonly Regex ArtikelN = new Regex(" (?:Artikel\\ |Art\\.\\ |§\\ )(\\d+\\w?)(\\ |\\,|\\.)");
	private static readonly Regex UndAlphaNum = new Regex(" und (\\d+\\w?)(\\ |\\,|\\.)");

	public static void ReferenceDetector() {
		foreach ((LawRef context, string searchText) in Program.toProcess) {
			foreach (Match match in ArtikelN.Matches(searchText)) {
				string following = searchText.Substring(match.Index + match.Length);
				string paragraph = match.Groups[1].Value;

				HandleArtikelNMatch(following, context, paragraph, match.Groups[2].Value == ",");
			}
		}
	}

	static void HandleArtikelNMatch(string following, LawRef lawDefinedIn, string paragraph, bool cont) {
		if (following.StartsWith("Absatz") || following.StartsWith("Abs.")) {
			following = following.Substring(following.IndexOf(' ') + 1);
			Match absMatch = AlphaNum.Match(following);
			if (absMatch.Success && absMatch.Index == 0) {
				AddReference(lawDefinedIn,
					new LawRef {
						shorthand = lawDefinedIn.shorthand,
						paragraph = paragraph, subparagraph = absMatch.Groups[1].Value
					});
			}
			else {
				AddReference(lawDefinedIn,
					new LawRef {
						shorthand = lawDefinedIn.shorthand,
						paragraph = paragraph
					});
				return;
			}

			if (following.IndexOfAny(new char[] {' ', '.'}) != -1 && following[following.IndexOfAny(new char[] {' ', '.'})] == ' ') {
				following = following.Substring(following.IndexOf(' ') + 1);
				if (following.StartsWith("und")) {
					Match abs2Match = AlphaNumEnd.Match(following, 4);
					if (abs2Match.Success || abs2Match.Index == 4) {
						AddReference(lawDefinedIn,
							new LawRef {
								shorthand = lawDefinedIn.shorthand,
								paragraph = paragraph, subparagraph = abs2Match.Groups[1].Value
							});
					}
				}
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

	private static bool AddReference(LawRef references, LawRef referenced) {
		Paragraph firstPar = Program.root.statues.FirstOrDefault(x => referenced.shorthand == x.shorthand)?.paragraphs
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

		Program.refcnt++;
		return true;
	}
}
}