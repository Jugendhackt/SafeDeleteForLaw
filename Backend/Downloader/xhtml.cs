using System.Linq;
//To be replaced with some 3rd party lib in the future
namespace Downloader {
public class Xhtml {
	/// <summary>
	/// collection of escape codes that do not work for xml
	/// </summary>
	public static (string, string)[] EscapeSequences = {
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
		("ÿ", "&yuml;"), ("€", "&euro;"), (" ", "&nbsp;")
	};
/// <summary>
/// De-escapes characters
/// </summary>
/// <param name="xhtml">The string to de-escape</param>
/// <returns>The fixed string</returns>
	public static string XhtmlReplace(string xhtml) => EscapeSequences.Aggregate(xhtml,
		(current, escapeSequence) => current.Replace(escapeSequence.Item2, escapeSequence.Item1));
}
}