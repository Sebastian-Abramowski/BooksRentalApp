package org.openjfx.requests;

import org.openjfx.database.User;
import org.openjfx.database.Book;
import org.openjfx.database.Wish;

public class AddWish extends Request {
	public static Wish request(User user, Book book, int days) {
		String query = "INSERT INTO WISH (user_id, book_id, days) " +
					   "VALUES (%d, %d, %d) ";
		query = String.format(query, user.getID(), book.getId(), days);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetWish.request(user, book);
		} else {
			return null;
		}
	}
}
