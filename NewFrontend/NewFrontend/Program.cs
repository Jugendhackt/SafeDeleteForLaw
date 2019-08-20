using System;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Threading;
using System.Threading.Tasks;
using DataStructures;
using Downloader;

namespace NewFrontend {
public class Program {
	[STAThread]
	public static void Main(string[] args) {
		if (!File.Exists(Path.Combine(DataStructures.JsonRoot.LawPath, "MetaOnly.json"))) {
			var process = new System.Diagnostics.Process()
				{StartInfo = new ProcessStartInfo(Path.Combine(Assembly.GetAssembly(typeof(Download)).Location), "Download.exe")};
			process.Start();
			process.WaitForExit();
		}

		new MainWindow().ShowDialog();
	}
}
}