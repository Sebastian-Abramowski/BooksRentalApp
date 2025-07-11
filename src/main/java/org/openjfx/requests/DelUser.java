package org.openjfx.requests;

import org.openjfx.database.User;

public class DelUser extends Request {
	public static Boolean request(User user) {
		String query = "DELETE FROM \"USER\" " +
					   "WHERE id = %d ";
		query = String.format(query, user.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
