using System;
using System.Collections.Generic;

namespace DataStructures {
public class JsonRoot {
	public List<Statue> Statues;
	public DateTime CreatedAt;

	public JsonRoot() {
		CreatedAt=DateTime.Now;
		Statues= new List<Statue>();
	}
}
}