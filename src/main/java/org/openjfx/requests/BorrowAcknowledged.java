package org.openjfx.requests;

import org.openjfx.database.Borrow;

/*
 * User clicks off the notification. Do not show it again.
 */
public class BorrowAcknowledged extends Request {
	public static Borrow request(Borrow borrow) {
		String query = "UPDATE BORROW " +
					   "SET acknowledged = true " +
					   "WHERE id = %d ";
		query = String.format(query, borrow.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBorrow.request(borrow);
		} else {
			return null;
		}
	}
}
