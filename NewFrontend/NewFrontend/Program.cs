using System;
using System.IO;
using System.Threading;
using System.Threading.Tasks;
using DataStructures;
using Downloader;

namespace NewFrontend {
public class Program {
	[STAThread]
	public static void Main(string[] args) {
		if (!File.Exists(Path.Combine(DataStructures.JsonRoot.LawPath, "MetaOnly.json"))) {
			System.Diagnostics.Process.Start("Download.exe");
		}

		new MainWindow().ShowDialog();
	}
}
}