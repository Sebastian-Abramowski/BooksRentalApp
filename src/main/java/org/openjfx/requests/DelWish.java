package org.openjfx.requests;

import org.openjfx.database.Wish;

public class DelWish extends Request {
	public static Boolean request(Wish wish) {
		String query = "DELETE FROM WISH " +
					   "WHERE wish_id = %d ";
		query = String.format(query, wish.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
