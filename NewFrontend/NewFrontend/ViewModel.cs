using DataStructures;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Downloader;
using  Newtonsoft.Json;

namespace NewFrontend
{
    public class ViewModel
    {
        public List<Statue> statues { get; set; }

        public ViewModel() {
            JsonRoot r = JsonConvert.DeserializeObject<JsonRoot>(File.ReadAllText(Path.Combine(DataStructures.JsonRoot.LawPath, "MetaOnly.json")));
            statues = r.statues;
        }
    }
}
