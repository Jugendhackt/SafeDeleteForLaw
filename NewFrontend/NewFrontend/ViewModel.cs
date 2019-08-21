using DataStructures;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using Downloader;
using Newtonsoft.Json;

namespace NewFrontend
{
    public class ViewModel : INotifyPropertyChanged
    {
        private Statue _statueSelectionCache;

        public Statue StatueSelectionCache {
            get => _statueSelectionCache;
            set {
                if (value is null) {
                    return;
                }

                _statueSelectionCache = value;
                OnPropertyChanged();
            }
        }

        public List<Statue> statues { get; set; }

        public ViewModel() {
            JsonRoot r = JsonConvert.DeserializeObject<JsonRoot>(File.ReadAllText(Path.Combine(DataStructures.JsonRoot.LawPath, "MetaOnly.json")));
            statues = r.statues;
            
        }

        public event PropertyChangedEventHandler PropertyChanged;

       // [NotifyPropertyChangedInvocator]
        protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null) {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
