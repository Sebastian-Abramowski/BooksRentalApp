package org.openjfx.helpers;

import java.util.ArrayList;
import java.util.List;

public class Filter {
	public static <T extends Searchable> ArrayList<T> match(List<T> collection, String key) {
		key = key.toLowerCase();
		var output = new ArrayList<T>();
		for (var element : collection) {
			for (var parm : element.getSearchParams()) {
				if (parm.toLowerCase().contains(key)) {
					output.add(element);
					break;
				}
			}
		}
		return output;
	}
}
