package org.openjfx.requests;

import org.openjfx.database.Person;

public class DelPerson extends Request {
	public static Boolean request(Person person) {
		String query = "DELETE FROM PERSON " +
					   "WHERE id = %d ";
		query = String.format(query, person.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
