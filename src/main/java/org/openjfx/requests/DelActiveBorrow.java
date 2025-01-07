package org.openjfx.requests;

import org.openjfx.database.Admin;
import org.openjfx.database.Borrow;

public class DelActiveBorrow extends Request {
	public static Boolean request(Borrow borrow, Admin user) {
		String query = "DELETE FROM ACTIVE_BORROW " +
					   "WHERE id = %d;";
		query = String.format(query, borrow.getId());
		if (addToArchived(borrow.getId(), user).equals( Boolean.FALSE )) return Boolean.FALSE;
		int result = executeUpdate(query);
		return result == 1;
	}

	private static Boolean addToArchived(int id, Admin user){
		String query = "INSERT INTO ARCHIVED_BORROW (id, approver_id) VALUES (%d, %d) ";
		query = String.format(query, id, user.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
