package org.openjfx.requests;

import org.openjfx.database.User;

public class AddUser extends Request {
	public static User request(String login, String password) {
		String query = "INSERT INTO \"USER\" (login, password) " +
					   "VALUES ('%s', '%s') ";
		query = String.format(query, login, password);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetUser.request(login);
		} else {
			return null;
		}
	}
}
