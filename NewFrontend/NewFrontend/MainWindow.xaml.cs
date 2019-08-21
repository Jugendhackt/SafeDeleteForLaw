using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using DataStructures;
using Downloader;
using Processor;
using Paragraph = DataStructures.Paragraph;

namespace NewFrontend
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }


        private void ListView_SelectionChanged(object sender, SelectionChangedEventArgs e) {
            var statueViewSelectedItem = (Statue) (StatueView.SelectedItem);
            /*   
            List<Paragraph> paragraphs = ((Statue) (statueView.SelectedItem)).paragraphs;
           // paragraphView.ItemsSource = paragraphs;
            if (!paragraphs.Any()) {
                Debug.WriteLine("Empty Statue");
                return;
            }
            Debug.WriteLine(paragraphs.First());*/
        }

        private void Selector_OnSelectionChanged(object sender, SelectionChangedEventArgs e) {
            foreach (object item in e.AddedItems) {
                switch (item) {
                    case TabItem _:
                        SearchBox.Text = string.Empty;
                        return;
                    case Statue _:
                        TControl.SelectedIndex = 1;
                        return;
                    
                    case Paragraph _:
                        TControl.SelectedIndex = 2;
                        return;
                    
                    case Subparagraph _:
                        TControl.SelectedIndex = 3;
                        return;
                }
            }

        }
    }
}
