using System.Collections.Generic;

namespace DataStructures {
public class Subparagraph {
	public string Number;
	public List<LawRef> RequiredBy;

	public Subparagraph() {
		RequiredBy= new List<LawRef>();
	}
}
}