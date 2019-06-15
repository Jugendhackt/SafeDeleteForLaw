using System;
using System.Collections.Generic;

namespace DataStructures {
public class Statue {
	public List<Paragraph> Paragraphs;
	public string Fullname,Shorthand;
	public DateTime BuildDate;

	public Statue() {
		Paragraphs= new List<Paragraph>();
	}
}
}