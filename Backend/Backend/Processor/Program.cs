using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Xml.Linq;
using DataStructures;

namespace Processor {
class Program {
	private static Dictionary<LawRef, List<string>> toProcess = new Dictionary<LawRef, List<string>>();
	private static JsonRoot root;

	static void Main(string[] args) {
		Console.WriteLine("Hello World!");
		root = new JsonRoot();
		foreach (string file in Directory.GetFiles(Downloader.Download.LawPath)) {
			XDocument currentFile = XDocument.Load(file);
			LoadMetaData(currentFile);
		}
	}

	static void LoadMetaData(XDocument file) {
		Statue toUse = null;
		XElement metadata = file.Root?.Element("norm")?.Element("metadaten");
		if (metadata is null) {
			return;
		}

		string shorthand = metadata.Element("jurabk").Value;
		string fullname = metadata.Element("langue").Value;

		toUse = new Statue();
		root.Statues.Add(toUse);

		toUse.Fullname = fullname;
		toUse.Shorthand = shorthand;
		LoadParagraphMetadata(file, toUse);
	}

	static void AddProcess(LawRef r, string s) {
		if (toProcess.ContainsKey(r)) {
			toProcess[r].Add(s);
		}
		else {
			toProcess.Add(r, new List<string> {s});
		}
	}

	/// <summary>
	/// Later to be used to remove inline xml
	/// </summary>
	/// <param name="org"></param>
	/// <returns></returns>
	static string rmXml(string org) => org;

	static void LoadParagraphMetadata(XDocument file, Statue toUse) {
		if (file.Root is null) {
			return;
		}

		foreach (XElement norm in file.Root.Elements("norm")) {
			if (norm.Element("metadaten")?.Element("enbez")?.Value is null) {
				continue;
			}

			string enbez = norm.Element("metadaten").Element("enbez").Value;
			if (enbez.Contains("(XXXX)") || enbez.Contains("Inhalt") ||enbez=="Übersicht") {
				continue;
			}

			string number = norm.Element("metadaten").Element("enbez").Value;
			if (number.StartsWith("§ ")) {
				number = number.Substring(2);
			}

			var p = new Paragraph {Number = number};
			toUse.Paragraphs.Add(p);
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

						p.HasSubparagraphs = true;
						p.Subparagraphs.Add(new Subparagraph {Number = subpar});
						if (subpar.Length + 3 >= xmlFreeVal.Length) {
							continue;
						}

						xmlFreeVal = xmlFreeVal.Substring(xmlFreeVal.IndexOf(')') + 2);
					}

					if (!string.IsNullOrWhiteSpace(xmlFreeVal)) {
						AddProcess(new LawRef {Paragraph = p.Number, Shorthand = toUse.Shorthand, Subparagraph = subpar}, xmlFreeVal);
					}
				}
			}
//TODO footnotes
			if (subpar is null) {
				p.Subparagraphs.Add(new Subparagraph());
			}
		}
	}
}
}