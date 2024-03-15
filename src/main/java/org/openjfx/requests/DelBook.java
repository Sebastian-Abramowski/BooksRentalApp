package org.openjfx.requests;

import org.openjfx.database.Book;

public class DelBook extends Request {
	public static Boolean request(Book book) {
		String query = "DELETE FROM BOOK " +
					   "WHERE book_id = %d ";
		query = String.format(query, book.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
