package org.openjfx.requests;

import org.openjfx.database.User;
import org.openjfx.database.Admin;

public class AddAdmin extends Request {
	public static Admin request(User user) {
		String query = "INSERT INTO ADMIN (id) " +
					   "VALUES (%d) ";
		query = String.format(query, user.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetAdmin.request(user);
		} else {
			return null;
		}
	}
}
