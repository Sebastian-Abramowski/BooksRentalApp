package org.openjfx.requests;

import org.openjfx.database.Book;

public class AddBookInstance extends Request {
	/*
	 * Adds a book instance to the database.
	 *
	 * It is striking that this is probably the only request that does not
	 * return anything. This is because it's very hard in this scenario, and
	 * there are no use cases for it.
	 *
	 * Ways of obtaining a book instance:
	 * 1. GetFirstBookInstance.request(book) - for transfering from wish -> borrow
	 * 2. GetBookInstances.* requests - for displaying book instances if you want.
	 */
	public static Boolean request(Book book) {
		String query = "INSERT INTO BOOK_INSTANCE (book_id) " +
					   "VALUES (%d) ";
		query = String.format(query, book.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
