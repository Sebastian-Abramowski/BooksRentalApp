package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Category;
import org.openjfx.database.ErrorHandler;

public class GetCategory extends Request {
	public static Category fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Category(
				result.getInt(1),
				result.getString(2)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Category request(int category_id) {
		String query = "SELECT * FROM CATEGORY " +
					   "WHERE id = %d ";
		query = String.format(query, category_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Category request(String category_name) {
		String query = "SELECT * FROM CATEGORY " +
					   "WHERE name = '%s' ";
		query = String.format(query, category_name);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Category request(Category category) {
		return request(category.getId());
	}
}
