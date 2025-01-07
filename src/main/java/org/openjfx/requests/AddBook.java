package org.openjfx.requests;

import org.openjfx.database.Book;
import org.openjfx.database.Category;

public class AddBook extends Request {
	public static Book request(String isbn, String title, int rating, Category category) {
		String query = "INSERT INTO BOOK (isbn, title, rating, category_id) " +
					   "VALUES ('%s', '%s', %d, %d) ";
		query = String.format(query, isbn, title, rating, category.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBook.request(isbn);
		} else {
			return null;
		}
	}
}
