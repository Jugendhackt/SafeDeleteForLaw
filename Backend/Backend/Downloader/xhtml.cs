using System.Linq;

namespace Downloader {
public class xhtml {
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
		("ÿ", "&yuml;"), ("€", "&euro;"), (" ", "&nbsp;")
	};

	public static string xhtmlReplace(string xhtml) {
		return Downloader.xhtml.escapeSequences.Aggregate(xhtml,
			(current, escapeSequence) => current.Replace(escapeSequence.Item2, escapeSequence.Item1));
	}
}
}