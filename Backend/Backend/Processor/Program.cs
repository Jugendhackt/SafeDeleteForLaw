using System;
using System.IO;
using System.Xml.Linq;
using DataStructures;

namespace Processor {
class Program {
	private static JsonRoot root;
	static void Main(string[] args) {
		Console.WriteLine("Hello World!");
		root= new JsonRoot( );
		foreach (string file in Directory.GetFiles(Downloader.Download.LawPath)) {
			XDocument currentFile=XDocument.Load(file);
		}
	}

	static void LoadMetaData(XDocument file) {
		XElement metadata = file.Root?.Element("norm")?.Element("metadaten");
		if (metadata is null) {
			return;
		}
		string shorthand=metadata.Element("jurabk").Value;
		string fullname = metadata.Element("langue").Value;
	}
}
}