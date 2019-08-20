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
		IEnumerable objects = (IEnumerable) values[0];
		String s = (string) values[1];
		return objects.Cast<object>() .Where(x => x.ToString().Contains(s)).Select(x=>x.ToString());
	}

	public object[] ConvertBack(object value, Type[] targetTypes, object parameter, CultureInfo culture) => throw new NotImplementedException();
}
}