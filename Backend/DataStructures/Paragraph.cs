using System.Collections.Generic;

namespace DataStructures {
public class Paragraph {
	public string number;
	public bool hasSubparagraphs;
	public List<Subparagraph> subparagraphs { get; set; }

	public Paragraph() {
		subparagraphs= new List<Subparagraph>();
	}

	public override string ToString() => $"§ {number}";
}
}