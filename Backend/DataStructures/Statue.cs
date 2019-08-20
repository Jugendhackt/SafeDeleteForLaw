using System;
using System.Collections.Generic;

namespace DataStructures {
public class Statue {
	public List<Paragraph> paragraphs { get; set; }
	public string fullname,shorthand;
	public DateTime builddate;

	public Statue() {
		paragraphs= new List<Paragraph>();
	}

	public override string ToString() => shorthand + ": "+fullname;
}
}