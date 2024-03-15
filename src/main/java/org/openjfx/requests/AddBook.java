package org.openjfx.requests;

import org.openjfx.database.Book;

public class AddBook extends Request {
	public static Book request(String title, String author, String category, int rating, int amount) {
		String query = "INSERT INTO BOOK (title, author, category, rating, amount) " +
					   "VALUES ('%s', '%s', '%s', %d, %d) ";
		query = String.format(query, title, author, category, rating, amount);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBook.request(title, author);
		} else {
			return null;
		}
	}
}
