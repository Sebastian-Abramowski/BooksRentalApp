package org.openjfx.requests;

import org.openjfx.database.Book;

public class ModifyBookRating extends Request {
	public static Book request(int rating, int book_id) {
		String query = "UPDATE BOOK set rating= %d WHERE book_id=%d";
		query = String.format(query, rating, book_id);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBook.request(book_id);
		} else {
			return null;
		}
	}
}
