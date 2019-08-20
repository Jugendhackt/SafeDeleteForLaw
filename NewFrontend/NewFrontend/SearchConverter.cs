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
			if (!objects.Cast<object>().Any()) {
				return "Keine Einträge";
			}
			String s = (string) values[1];
			return objects.Cast<object>() .Where(x => x.ToString().Contains(s));

		}
	}

	public object[] ConvertBack(object value, Type[] targetTypes, object parameter, CultureInfo culture) => throw new NotImplementedException();
}
}