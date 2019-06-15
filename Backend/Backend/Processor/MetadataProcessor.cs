using System;
using System.Linq;
using System.Xml.Linq;
using DataStructures;

namespace Processor {
public class MetadataProcessor {
	public static void LoadMetaData(XDocument file) {
		Statue toUse = null;
		XElement metadata = file.Root?.Element("norm")?.Element("metadaten");
		if (metadata is null) {
			return;
		}

		string shorthand = metadata.Element("jurabk").Value;
		string fullname = metadata.Element("langue").Value.Replace('\n', ' ');

		toUse = new Statue();
		Program.root.statues.Add(toUse);

		toUse.fullname = fullname;
		toUse.shorthand = shorthand;
		LoadParagraphMetadata(file, toUse);
	}

	/// <summary>
	/// Later to be used to remove inline xml
	/// </summary>
	/// <param name="org"></param>
	/// <returns></returns>
	private static string rmXml(string org) => org;

	private static void LoadParagraphMetadata(XDocument file, Statue toUse) {
		if (file.Root is null) {
			return;
		}

		foreach (XElement norm in file.Root.Elements("norm")) {
			if (norm.Element("metadaten")?.Element("enbez")?.Value is null) {
				continue;
			}

			string enbez = norm.Element("metadaten").Element("enbez").Value;
			if (enbez.Contains("(XXXX)") || enbez.Contains("Inhalt") || enbez == "Übersicht") {
				continue;
			}

			string number = norm.Element("metadaten").Element("enbez").Value;
			if (number.StartsWith("§ ")) {
				number = number.Substring(2);
			}

			if (number.StartsWith("Art. ")) {
				number = number.Substring(5);
			}

			var p = new Paragraph {number = number};
			toUse.paragraphs.Add(p);
			XElement textData = norm.Element("textdaten");
			if (textData is null || textData.IsEmpty) {
				continue;
			}

			string subpar = null;
			if (textData.Element("text") != null) {
				foreach (XElement pars in textData.Element("text").Element("Content").Elements("P")) {
					string xmlFreeVal = rmXml(pars.Value);
					if (xmlFreeVal.StartsWith('(') && xmlFreeVal.Contains(')') && xmlFreeVal.IndexOf(')') < 5) {
						subpar = xmlFreeVal.Substring(1, xmlFreeVal.IndexOf(')') - 1);

						p.hasSubparagraphs = true;
						p.subparagraphs.Add(new Subparagraph {number = subpar});
						if (subpar.Length + 3 >= xmlFreeVal.Length) {
							continue;
						}

						xmlFreeVal = xmlFreeVal.Substring(xmlFreeVal.IndexOf(')') + 2);
					}

					if (!String.IsNullOrWhiteSpace(xmlFreeVal)) {
						Program.toProcess.Add((new LawRef {paragraph = p.number, shorthand = toUse.shorthand, subparagraph = subpar},
							xmlFreeVal));
					}
				}
			}

			if (textData.Element("footnotes") != null && !textData.Element("footnotes").IsEmpty) {
				Program.toProcess.AddRange(textData.Element("fussnoten").Element("Content").Elements("P").Select(x => x.Value)
					.Where(x => !String.IsNullOrWhiteSpace(x))
					.Select(x => (new LawRef {paragraph = p.number, shorthand = toUse.shorthand}, x)));
			}

//TODO footnotes
			if (subpar is null) {
				p.subparagraphs.Add(new Subparagraph());
			}
		}
	}
}
}