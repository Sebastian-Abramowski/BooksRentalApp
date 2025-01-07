package org.openjfx.requests;

import org.openjfx.database.Client;
import org.openjfx.database.Admin;
import org.openjfx.database.Borrow;
import org.openjfx.database.BookInstance;

public class AddBorrow extends Request {
	public static Borrow request(BookInstance book_instance, int days, Admin approver, Client client) {
		String query = "INSERT INTO BORROW (book_instance_id, days, approver_id, client_id) " +
					   "VALUES (%d, %d, %d, %d) ";
		query = String.format(query, book_instance.getId(), days, approver.getId(), client.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBorrow.request(book_instance);
		} else {
			return null;
		}
	}
}
