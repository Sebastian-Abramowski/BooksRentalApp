package org.openjfx.requests;

import org.openjfx.database.User;
import org.openjfx.database.Client;

public class AddClient extends Request {
	public static Client request(User user, String phone_number) {
		String query = "INSERT INTO CLIENT (id, phone_number) " +
					   "VALUES (%d, '%s') ";
		query = String.format(query, user.getId(), phone_number);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetClient.request(user);
		} else {
			return null;
		}
	}
}
