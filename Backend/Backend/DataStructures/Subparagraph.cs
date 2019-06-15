using System.Collections.Generic;

namespace DataStructures {
public class Subparagraph {
	public string number;
	public List<LawRef> requiredby;

	public Subparagraph() {
		requiredby= new List<LawRef>();
	}
}
}