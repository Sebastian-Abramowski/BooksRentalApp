package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.User;
import org.openjfx.database.Person;
import org.openjfx.database.ErrorHandler;

public class GetUser extends Request {
	public static User fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new User(
				result.getInt(1),
				result.getString(2),
				result.getString(3),
				result.getInt(4)
			);
		} catch(SQLException e) {
			System.out.println("DUPA " + result.toString());
			new ErrorHandler(e);
			return null;
		}
	}

	public static User request(int user_id) {
		String query = "SELECT * FROM \"USER\" " +
					   "WHERE id = %s ";
		query = String.format(query, user_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static User request(User user) {
		return request(user.getId());
	}

	public static User request(String login) {
		String query = "SELECT * FROM \"USER\" " +
					   "WHERE login = '%s' ";
		query = String.format(query, login);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static User request(String login, String password) {
		String query = "SELECT * FROM \"USER\" " +
				       "WHERE login = '%s' AND password = '%s'";
		query = String.format(query, login, password);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static User from(int person_id) {
		String query = "SELECT * FROM \"USER\" " +
					   "WHERE person_id = %s ";
		query = String.format(query, person_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static User from(Person person) {
		return from(person.getId());
	}
}
