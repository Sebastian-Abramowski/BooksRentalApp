package org.openjfx.requests;

import org.openjfx.database.BookAuthor;

public class DelBookAuthor extends Request {
	public static Boolean request(BookAuthor book_author) {
		String query = "DELETE FROM BOOK_AUTHOR " +
					   "WHERE book_id = %d AND person_id = %d ";
		query = String.format(query, book_author.getBookId(), book_author.getPersonId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
