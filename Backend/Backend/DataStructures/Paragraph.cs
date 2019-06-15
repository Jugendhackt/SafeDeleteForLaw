using System.Collections.Generic;

namespace DataStructures {
public class Paragraph {
	public string Number;
	public bool HasSubparagraphs;
	public List<Subparagraph> Subparagraphs;

	public Paragraph() {
		Subparagraphs= new List<Subparagraph>();
	}
	}
}