package org.openjfx.requests;

import org.openjfx.database.User;
import org.openjfx.database.Book;
import org.openjfx.database.Borrow;

public class AddBorrow extends Request {
	public static Borrow request(User user, Book book, int days) {
		String query = "INSERT INTO BORROW (user_id, book_id, days) " +
					   "VALUES (%d, %d, %d) ";
		query = String.format(query, user.getID(), book.getId(), days);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBorrow.request(user, book);
		} else {
			return null;
		}
	}
}
