package org.openjfx.requests;

import org.openjfx.database.Book;

public class ChangeBookAmount extends Request {
	public static Book request(Book book, int delta) {
		String query = "UPDATE BOOK " +
					   "SET amount = amount + (%d) " +
					   "WHERE book_id = %d ";
		query = String.format(query, delta, book.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBook.request(book);
		} else {
			return null;
		}
	}

	public static int request(int bookId, int delta) {
		String query = "UPDATE BOOK " +
				"SET amount = amount + (%d) " +
				"WHERE book_id = %d ";
		query = String.format(query, delta, bookId);
		int result = executeUpdate(query);
		if(result == 1) {
			return 1;
		} else {
			return -1;
		}
	}
}
