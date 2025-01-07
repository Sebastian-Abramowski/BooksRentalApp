package org.openjfx.requests;

import org.openjfx.database.Category;

public class DelCategory extends Request {
	public static Boolean request(Category category) {
		String query = "DELETE FROM CATEGORY " +
					   "WHERE id = %d ";
		query = String.format(query, category.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
