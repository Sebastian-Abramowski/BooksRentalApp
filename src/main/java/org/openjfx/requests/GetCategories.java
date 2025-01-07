package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import org.openjfx.database.Category;
import org.openjfx.database.Book;

public class GetCategories extends Request {
	public static ArrayList<Category> fromResult(ResultSet result) {
		ArrayList<Category> categories = new ArrayList<>();
		Category category;
		while((category = GetCategory.fromResult(result)) != null) {
			categories.add(category);
		}
		return categories;
	}

	public static ArrayList<Category> request() {
		String query = "SELECT * FROM CATEGORY ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Category> fromBooks(ArrayList<Book> books) {
		HashSet<Category> category_set = new HashSet<>();
		ArrayList<Category> categories = new ArrayList<>();
		for(Book book : books) {
			Category category = book.getCategory();
			if(category_set.add(category)) {
				categories.add(category);
			}
		}
		return categories;
	}
}
