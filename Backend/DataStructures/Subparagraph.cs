using System.Collections.Generic;

namespace DataStructures {
public class Subparagraph {
	public string number;
	public List<LawRef> requiredby;

	public void AddRequired(LawRef toAdd) {
		if (requiredby.Contains(toAdd)) {
			return;
		}
		requiredby.Add(toAdd);
	}
	public Subparagraph() {
		requiredby= new List<LawRef>();
	}

    public override string ToString()=>$"§§ {number}";
}
}