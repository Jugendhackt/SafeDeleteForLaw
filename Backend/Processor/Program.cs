#define SeperatedReferenceDetection
#define AdvancedMode
#if AdvancedMode
#define InMemoryXmlHandling
#if DEBUG

#else
#define MultiThreadingEnabled
#endif
#endif
using System;
using System.Collections.Concurrent;
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
	public static ConcurrentBag<(LawRef, string)> toProcess = new ConcurrentBag<(LawRef, string)>();

	public static JsonRoot root = new JsonRoot();
	public static int refcnt;

	static void Main(string[] args) {
#if InMemoryXmlHandling &&!SeperatedReferenceDetection
		Console.WriteLine("In Memory Xml Handling is enabled this executable has nothing to do, exiting with 1");
		Environment.Exit(1);
#endif
ActualReferences();

File.WriteAllText("root.json", JsonConvert.SerializeObject(root));
		Stats();
	}

	public static JsonRoot ActualReferences() {
		ReferenceProcessor.ReferenceDetector();
#if false
		#if SeperatedReferenceDetection
      		root = JsonConvert.DeserializeObject<JsonRoot>(File.ReadAllText(Path.Combine(DataStructures.JsonRoot.LawPath,
      			"MetaOnly.json")));
      		toProcess = new ConcurrentBag<(LawRef, string)>(
      			JsonConvert.DeserializeObject<(LawRef, string)[]>(File.ReadAllText(Path.Combine(DataStructures.JsonRoot.LawPath,
      				"TextOnly.json"))));
      #else
      		foreach (string file in Directory.GetFiles(JsonRoot.LawPath)) {
            			XDocument currentFile = XDocument.Load(file);
            			MetadataProcessor.LoadMetaData(currentFile);
            		}
      #endif
      #if MultiThreadingEnabled
      		ReferenceProcessor.MCReferenceDetector();
      #else
      		ReferenceProcessor.ReferenceDetector();
      #endif
#endif

		return root;
	}

	public static void Stats() {
		Console.WriteLine(
			$"Found {root.statues.Sum(x => x.paragraphs.Count)} paragraphs {root.statues.Sum(x => x.paragraphs.Sum(y => y.subparagraphs.Count))}" +
			$" subpars and {toProcess.Count} textblocks and {refcnt} references, the json is {new FileInfo("root.json").Length:N0} byte big");
	}
}
}