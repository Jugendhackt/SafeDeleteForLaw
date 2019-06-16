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
	/// <summary>
	/// All text to process
	/// </summary>
	public static List<(LawRef, string)> toProcess = new List<(LawRef, string)>();
	public static JsonRoot root;
	public static int refcnt;

	static void Main(string[] args) {
		Console.WriteLine("Hello World!");
		root = new JsonRoot();
		foreach (string file in Directory.GetFiles(Downloader.Download.LawPath)) {
			XDocument currentFile = XDocument.Load(file);
			MetadataProcessor.LoadMetaData(currentFile);
		}

		ReferenceProcessor.ReferenceDetector();

		File.WriteAllText("root.json", JsonConvert.SerializeObject(root));
		Console.WriteLine(
			$"Found {root.statues.Sum(x => x.paragraphs.Count)} paragraphs {root.statues.Sum(x => x.paragraphs.Sum(y => y.subparagraphs.Count))} subpars and {toProcess.Count} textblocks and {refcnt} references, the json is {new FileInfo("root.json").Length} byte big");
	}
}
}