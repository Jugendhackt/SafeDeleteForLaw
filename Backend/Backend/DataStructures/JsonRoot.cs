using System;
using System.Collections.Generic;

namespace DataStructures {
public class JsonRoot {
	public List<Statue> statues;
	public DateTime createdat;

	public JsonRoot() {
		createdat=DateTime.Now;
		statues= new List<Statue>();
	}
}
}