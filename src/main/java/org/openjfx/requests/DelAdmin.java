package org.openjfx.requests;

import org.openjfx.database.Admin;

public class DelAdmin extends Request {
	public static Boolean request(int user_id) {
		String query = "DELETE FROM ADMIN " +
					   "WHERE id = %d ";
		query = String.format(query, user_id);
		int result = executeUpdate(query);
		return result == 1;
	}

	public static Boolean request(Admin admin) {
		return request(admin.getId());
	}
}
