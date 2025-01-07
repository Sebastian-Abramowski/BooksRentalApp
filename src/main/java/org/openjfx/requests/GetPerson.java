package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Person;
import org.openjfx.database.ErrorHandler;

public class GetPerson extends Request {
	public static Person fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Person(
				result.getInt(1),
				result.getString(2),
				result.getString(3)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Person request(int person_id) {
		String query = "SELECT * FROM PERSON " +
					   "WHERE id = %d ";
		query = String.format(query, person_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Person request(String name, String surname) {
		String query = "SELECT * FROM PERSON " +
					   "WHERE name = '%s' AND surname = '%s' ";
		query = String.format(query, name, surname);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Person request(Person person) {
		return request(person.getId());
	}
}
