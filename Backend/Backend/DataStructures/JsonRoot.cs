using System;
using System.Collections.Generic;
using System.IO;

namespace DataStructures {
public class JsonRoot {
	public List<Statue> statues;
	public DateTime createdat;

	public JsonRoot() {
		createdat=DateTime.Now;
		statues= new List<Statue>();
	}

	public static readonly string LawPath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),
		"SafeDeleteForLaw");
}
}