using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace DataStructures {
public class Paragraph {
	private Regex NeedParSign= new Regex("\\d+\\w?");
	public string number;
	public bool hasSubparagraphs;
	public List<Subparagraph> subparagraphs { get; set; }

	public Paragraph() {
		subparagraphs= new List<Subparagraph>();
	}

	public override string ToString() {
		if (NeedParSign.IsMatch(number)) {
			return $"§ {number}";
		}
		else {
			return number;
		}
	}
}
}