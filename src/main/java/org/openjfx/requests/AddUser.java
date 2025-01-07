package org.openjfx.requests;

import org.openjfx.database.Person;
import org.openjfx.database.User;

public class AddUser extends Request {
	public static User request(String login, String password, Person person) {
		String query = "INSERT INTO \"USER\" (login, password, person_id) " +
					   "VALUES ('%s', '%s', %d) ";
		query = String.format(query, login, password, person.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetUser.request(login);
		} else {
			return null;
		}
	}
}
