package org.openjfx.requests;

import org.openjfx.database.BookInstance;

public class DelBookInstance extends Request {
	public static Boolean request(BookInstance book_instance) {
		String query = "DELETE FROM BOOK_INSTANCE " +
					   "WHERE id = %d ";
		query = String.format(query, book_instance.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
