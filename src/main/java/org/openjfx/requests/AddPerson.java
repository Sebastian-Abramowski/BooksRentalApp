package org.openjfx.requests;

import org.openjfx.database.Person;

public class AddPerson extends Request {
	public static Person request(String name, String surname) {
		String query = "INSERT INTO PERSON (name, surname) " +
					   "VALUES ('%s', '%s') ";
		query = String.format(query, name, surname);
		int result = executeUpdate(query);
		if(result == 1) {
			return GetPerson.request(name, surname);
		} else {
			return null;
		}
	}
}
