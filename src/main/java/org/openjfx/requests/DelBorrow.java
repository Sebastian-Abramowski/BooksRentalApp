package org.openjfx.requests;

import org.openjfx.database.Borrow;

public class DelBorrow extends Request {
	public static Boolean request(Borrow borrow) {
		String query = "DELETE FROM BORROW " +
					   "WHERE borrow_id = %d ";
		query = String.format(query, borrow.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
