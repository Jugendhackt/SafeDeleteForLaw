using DataStructures;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NewFrontend
{
    public class ViewModel
    {
        public List<Statue> statues { get; set; }
        public List<Paragraph> paragraphs { get; set; }
        public List<Subparagraph> subparagraphs { get; set; }

        public ViewModel()
        {
            JsonRoot r = Processor.Program.ActualReferences();
            statues = r.statues;
        }
    }
}
