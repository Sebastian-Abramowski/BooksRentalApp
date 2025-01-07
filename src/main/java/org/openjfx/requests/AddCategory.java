package org.openjfx.requests;

import org.openjfx.database.Category;

public class AddCategory extends Request {
	public static Category request(String name) {
		String query = "INSERT INTO CATEGORY (name) " +
					   "VALUES ('%s') ";
		query = String.format(query, name);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetCategory.request(name);
		} else {
			return null;
		}
	}
}
