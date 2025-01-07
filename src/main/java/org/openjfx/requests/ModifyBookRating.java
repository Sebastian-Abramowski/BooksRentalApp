package org.openjfx.requests;

import org.openjfx.database.Book;

public class ModifyBookRating extends Request {
	public static Book request(Book book, int rating) {
		String query = "UPDATE BOOK " +
					   "SET rating = %d " +
					   "WHERE id = %d ";
		query = String.format(query, rating, book.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBook.request(book);
		} else {
			return null;
		}
	}
}
