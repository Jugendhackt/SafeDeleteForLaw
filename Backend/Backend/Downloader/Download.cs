using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace Downloader {
public class Download {
	private const string divName = "{http://www.w3.org/1999/xhtml}div";
	private static string XmlZip = "/xml.zip";

	public static readonly string xhtmlIncompliant =
		"<li id=\"grDarst6\"><a href=\"http://www.justiz.de/onlinedienste/bundesundlandesrecht/index.php\" title=\"Die Startseite dieses Angebotes wird in einem neuen Fenster ge�ffnet\" target=\"_blank\" class=\"nav\">Landesrecht</a>";

	public static async Task Main(string[] args) {
		await DownloadAllLaws();
	}

	public static readonly Uri baseUrl = new Uri("https://www.gesetze-im-internet.de/");

	public static HttpClient Client = new HttpClient();

	public static readonly string LawPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),
		"SafeDeleteForLaw");

	/// <summary>
/// Download all Laws to <see cref="LawPath"/>
/// </summary>
/// <returns>a Task because its async</returns>
	public static async Task DownloadAllLaws() {
		Directory.CreateDirectory(LawPath);
		string akt = await Client.GetStringAsync(new Uri(baseUrl, "aktuell.html"));
		akt = xhtml.xhtmlReplace(akt).Replace(xhtmlIncompliant, "");
		XDocument aktDocument = XDocument.Parse(akt);
		XElement content = aktDocument.Root.Element("{http://www.w3.org/1999/xhtml}body").Elements()
			.First(x => x.Name == divName && x.Attribute("id").Value == "level2").Element(divName);
		IEnumerable<string> hrefs = content.Element(divName).Element(divName).Elements().SelectMany(x => x.Elements())
			.Where(x => x.Name == "{http://www.w3.org/1999/xhtml}a").Select(x => x.Attribute("href").Value);
		foreach (string href in hrefs) {
			await allLawsStartingWith(href);
		}

		Console.WriteLine($"Loaded {new DirectoryInfo(LawPath).GetFiles().Length} laws");
	}

	/// <summary>
/// Downloads all laws from one of the subpages
/// </summary>
/// <param name="href">The link to the subpage to load data from, example: https://www.gesetze-im-internet.de/Teilliste_I.html</param>
/// <returns>a task because this method is async</returns>
	public static async Task allLawsStartingWith(string href) {
		var allXmls = new List<string>();
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

			using Stream zipStream = unusedZipStream;
			var z = new ZipArchive(zipStream);
			foreach (ZipArchiveEntry zipArchiveEntry in z.Entries.Where(x => x.Name.EndsWith(".xml"))) {
				using Stream zipContentStream = zipArchiveEntry.Open();
				try {
					using FileStream fileStream = File.Create(Path.Combine(LawPath, zipArchiveEntry.Name),32000,FileOptions.None);
					await zipContentStream.CopyToAsync(fileStream);
					await fileStream.FlushAsync();
				}
				catch (IOException e) {
					Console.WriteLine(e);
					continue;
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