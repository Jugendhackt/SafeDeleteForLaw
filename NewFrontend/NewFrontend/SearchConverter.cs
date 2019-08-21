using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Windows.Data;
using DataStructures;

namespace NewFrontend {
public class SearchConverter : IMultiValueConverter {
	public object Convert(object[] values, Type targetType, object parameter, CultureInfo culture) {
		if (values[0] is IEnumerable objects) {


			String s = (string) values[1];
			Boolean hide = (bool) values[2];
			IEnumerable<object> matches = objects.Cast<object>().Where(x => x.ToString().IndexOf(s,StringComparison.OrdinalIgnoreCase)!= -1 &&
				(!hide||!(x is Statue stat && !stat.paragraphs.Any()) ));
			if (!matches.Any()) {
				return new[] {"Keine Einträge"};
			}
			return matches.OrderByDescending(x=> GetRanking(x,s));

		}
		else {
			return new[] {"Fehler"};
		}

	}

	private static int GetRanking(object x,string s) {
		switch (x) {
			case Statue stat:
				if (stat.shorthand.Equals(s,StringComparison.OrdinalIgnoreCase)) {
					return int.MaxValue;
				}

				int indexOf = stat.shorthand.IndexOf(s, StringComparison.OrdinalIgnoreCase);
				if (indexOf==0) {
					return 100;
				}

				if (indexOf!=-1) {
					return 10;
				}

				return 0;
			default: return int.MinValue;
		}
	}

	public object[] ConvertBack(object value, Type[] targetTypes, object parameter, CultureInfo culture) => throw new NotImplementedException();
}
}