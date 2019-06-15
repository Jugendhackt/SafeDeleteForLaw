using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace Downloader {
public class Download {
	private const string divName = "{http://www.w3.org/1999/xhtml}div";

	public static (string, string)[] escapeSequences = {
		("&", "&amp;"), (">", "&gt;"), ("¡", "&iexcl;"), ("¢", "&cent;"), ("£", "&pound;"), ("¤", "&curren;"), ("¥", "&yen;"),
		("¦", "&brvbar;"), ("§", "&sect;"), ("¨", "&uml;"), ("©", "&copy;"), ("ª", "&ordf;"), ("«", "&laquo;"), ("¬", "&not;"),
		("­", "&shy;"), ("®", "&reg;"), ("¯", "&macr;"), ("¡", "&iexcl;"), ("¢", "&cent;"), ("£", "&pound;"), ("¤", "&curren;"),
		("¥", "&yen;"), ("¦", "&brvbar;"), ("§", "&sect;"), ("¨", "&uml;"), ("©", "&copy;"), ("ª", "&ordf;"), ("«", "&laquo;"),
		("¬", "&not;"), ("­", "&shy;"), ("®", "&reg;"), ("¯", "&macr;"), ("À", "&Agrave;"), ("Á", "&Aacute;"), ("Â", "&Acirc;"),
		("Ã", "&Atilde;"), ("Ä", "&Auml;"), ("Å", "&Aring;"), ("Æ", "&AElig;"), ("Ç", "&Ccedil;"), ("È", "&Egrave;"),
		("É", "&Eacute;"), ("Ê", "&Ecirc;"), ("Ë", "&Euml;"), ("Ì", "&Igrave;"), ("Í", "&Iacute;"), ("Î", "&Icirc;"),
		("Ï", "&Iuml;"), ("Ð", "&ETH;"), ("Ñ", "&Ntilde;"), ("Ò", "&Ograve;"), ("Ó", "&Oacute;"), ("Ô", "&Ocirc;"),
		("Õ", "&Otilde;"), ("Ö", "&Ouml;"), ("×", "&times;"), ("Ø", "&Oslash;"), ("Ù", "&Ugrave;"), ("Ú", "&Uacute;"),
		("Û", "&Ucirc;"), ("Ü", "&Uuml;"), ("Ý", "&Yacute;"), ("Þ", "&THORN;"), ("ß", "&szlig;"), ("à", "&agrave;"),
		("á", "&aacute;"), ("â", "&acirc;"), ("ã", "&atilde;"), ("ä", "&auml;"), ("å", "&aring;"), ("æ", "&aelig;"),
		("ç", "&ccedil;"), ("è", "&egrave;"), ("é", "&eacute;"), ("ê", "&ecirc;"), ("ë", "&euml;"), ("ì", "&igrave;"),
		("í", "&iacute;"), ("î", "&icirc;"), ("ï", "&iuml;"), ("ð", "&eth;"), ("ñ", "&ntilde;"), ("ò", "&ograve;"),
		("ó", "&oacute;"), ("ô", "&ocirc;"), ("õ", "&otilde;"), ("ö", "&ouml;"), ("÷", "&divide;"), ("ø", "&oslash;"),
		("ù", "&ugrave;"), ("ú", "&uacute;"), ("û", "&ucirc;"), ("ü", "&uuml;"), ("ý", "&yacute;"), ("þ", "&thorn;"),
		("ÿ", "&yuml;"), ("€", "&euro;"),(" ","&nbsp;")
	};
	public static string xhtmlReplace(string xhtml) {
		return escapeSequences.Aggregate(xhtml, (current, escapeSequence) => current.Replace(escapeSequence.Item2, escapeSequence.Item1));
	}
	public static async Task Main(string[] args) {
		await DownloadAllLaws();
	}
	public static readonly Uri baseUrl = new Uri("https://www.gesetze-im-internet.de/");
	public static HttpClient Client=new HttpClient();
	private static readonly string xhtmlIncompliant = 
		"<li id=\"grDarst6\"><a href=\"http://www.justiz.de/onlinedienste/bundesundlandesrecht/index.php\" title=\"Die Startseite dieses Angebotes wird in einem neuen Fenster ge�ffnet\" target=\"_blank\" class=\"nav\">Landesrecht</a>";

	public static async Task DownloadAllLaws() {
		string akt =await Client.GetStringAsync(new Uri(baseUrl, "aktuell.html"));
		akt = xhtmlReplace(akt).Replace(xhtmlIncompliant,"");
		XDocument aktDocument= XDocument.Parse(akt);
		XElement content = aktDocument.Root.Element("{http://www.w3.org/1999/xhtml}body").Elements()
			.First(x => x.Name==divName&& x.Attribute("id").Value == "level2").Element(divName);
		IEnumerable<string> hrefs = content.Element(divName).Element(divName).Elements().SelectMany(x => x.Elements())
			.Where(x => x.Name == "{http://www.w3.org/1999/xhtml}a").Select(x => x.Attribute("href").Value);
		foreach (string href in hrefs) {
			await allLawsStartingWith(href);
		}
	}

	public static async Task<string[]> allLawsStartingWith(string href) {
		string xhtml =await Client.GetStringAsync(new Uri(baseUrl, href));
		xhtml = xhtmlReplace(xhtml).Replace(xhtmlIncompliant,"");
		XDocument aktDocument= XDocument.Parse(xhtml);
		XElement container = (XElement) aktDocument.Root.Element("{http://www.w3.org/1999/xhtml}body").Elements()
			.First(x => x.Name==divName&& x.Attribute("id").Value == "level2").Element(divName).Element(divName);
		IEnumerable<XNode> Laws = container.Element(divName).Nodes();
		return null;
	}
}
}