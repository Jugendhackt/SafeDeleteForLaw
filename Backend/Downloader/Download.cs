﻿#define AdvancedMode
#if AdvancedMode
#define MultiThreadingEnabled
#define InMemoryXmlHandling
#if DEBUG
#define SeperatedReferenceDetection
#endif
#endif

using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Xml.Linq;
using DataStructures;
using Newtonsoft.Json;

namespace Downloader {
public class Download {
	private const string divName = "{http://www.w3.org/1999/xhtml}div";
	private static string XmlZip = "/xml.zip";

	public static readonly string xhtmlIncompliant =
		"<li id=\"grDarst6\"><a href=\"http://www.justiz.de/onlinedienste/bundesundlandesrecht/index.php\" title=\"Die Startseite dieses Angebotes wird in einem neuen Fenster ge�ffnet\" target=\"_blank\" class=\"nav\">Landesrecht</a>";


	public static async Task Main(string[] args) {
		IEnumerable<string> hrefs = await DownloadLawListUrls();
#if MultiThreadingEnabled
		Task.WaitAll(hrefs.Select(x => Task.Run(() => AllLawsStartingWith(x))).ToArray());
#if SeperatedReferenceDetection
		File.WriteAllText(Path.Combine(DataStructures.JsonRoot.LawPath, "MetaOnly.json"),
			JsonConvert.SerializeObject(Processor.Program.root));
		File.WriteAllText(Path.Combine(DataStructures.JsonRoot.LawPath, "TextOnly.json"),
			JsonConvert.SerializeObject(Processor.Program.toProcess));
		Console.WriteLine("Text and Meta written");
#else
		Processor.ReferenceProcessor.MCReferenceDetector();
#endif
#if !InMemoryXmlHandling
		Processor.Program.Stats();
#endif
		
#else
		foreach (string href in hrefs) {
			await allLawsStartingWith(href);
		}
		Console.WriteLine($"Loaded {new DirectoryInfo(JsonRoot.LawPath).GetFiles().Length} laws");
#endif
	}

	public static readonly Uri baseUrl = new Uri("https://www.gesetze-im-internet.de/");

	public static HttpClient Client = new HttpClient();

	/// <summary>
	/// Download all Laws to <see cref="JsonRoot.LawPath"/>
	/// </summary>
	/// <returns>a Task because its async</returns>
	public static async Task<IEnumerable<string>> DownloadLawListUrls() {
		Directory.CreateDirectory(JsonRoot.LawPath);
		string akt = await Client.GetStringAsync(new Uri(baseUrl, "aktuell.html"));
		akt = xhtml.xhtmlReplace(akt).Replace(xhtmlIncompliant, "");
		XDocument aktDocument = XDocument.Parse(akt);
		XElement content = aktDocument.Root.Element("{http://www.w3.org/1999/xhtml}body").Elements()
			.First(x => x.Name == divName && x.Attribute("id").Value == "level2").Element(divName);
		return content.Element(divName).Element(divName).Elements().SelectMany(x => x.Elements())
			.Where(x => x.Name == "{http://www.w3.org/1999/xhtml}a").Select(x => x.Attribute("href").Value);
	}

	/// <summary>
	/// Downloads all laws from one of the subpages
	/// </summary>
	/// <param name="href">The link to the subpage to load data from, example: https://www.gesetze-im-internet.de/Teilliste_I.html</param>
	/// <returns>a task because this method is async</returns>
	public static async Task AllLawsStartingWith(string href) {
		Console.WriteLine(
			$"Running downloader for {href} on Thread {Thread.CurrentThread.Name} {Thread.CurrentThread.IsBackground} on Proc ");
		Thread.CurrentThread.Name = $"Worker for {href}";
		string xhtml = await Client.GetStringAsync(new Uri(baseUrl, href));
		xhtml = Downloader.xhtml.xhtmlReplace(xhtml).Replace(xhtmlIncompliant, "");
		XDocument aktDocument = XDocument.Parse(xhtml);
		XElement container = aktDocument.Root.Element("{http://www.w3.org/1999/xhtml}body").Elements()
			.First(x => x.Name == divName && x.Attribute("id").Value == "level2").Element(divName).Element(divName);
		IEnumerable<string> Laws = container.Element(divName).Elements()
			.Select(x => x.Element("{http://www.w3.org/1999/xhtml}a").Attribute("href").Value.Substring(2))
			.Select(x => new Uri(baseUrl, x.Substring(0, x.Length - 11)) + XmlZip);
		foreach (string law in Laws) {
			Console.WriteLine($"Loading {law}");
			Stream unusedZipStream;
			try {
				unusedZipStream = await Client.GetStreamAsync(law);
			}
			catch (HttpRequestException e) {
				Console.WriteLine($"Error when loading {law}: {e}");
				continue;
			}

			using (Stream zipStream = unusedZipStream) {
				var z = new ZipArchive(zipStream);
				foreach (ZipArchiveEntry zipArchiveEntry in z.Entries.Where(x => x.Name.EndsWith(".xml"))) {
					using (Stream zipContentStream = zipArchiveEntry.Open())
#if InMemoryXmlHandling
					{
						Processor.MetadataProcessor.LoadMetaData(XDocument.Load(zipContentStream));
					}
#else
				try {
					using FileStream fileStream =
						File.Create(Path.Combine(JsonRoot.LawPath, zipArchiveEntry.Name), 32000, FileOptions.None);
					await zipContentStream.CopyToAsync(fileStream);
					await fileStream.FlushAsync();
				}
				catch (IOException e) {
					Console.WriteLine(e);
					continue;
				}
#endif
				}
			}

			/*	using Stream xmlStream=z.Entries.First().Open();
				using var xmlReader=new StreamReader(xmlStream);
				allXmls.Add(await xmlReader.ReadToEndAsync());*/
			//	z.Entries.First()
		}
	}
}
}