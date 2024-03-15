package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Borrow;
import org.openjfx.database.ErrorHandler;
import org.openjfx.database.User;
import org.openjfx.database.Wish;

public class GetUser extends Request {
	public static User fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new User(
				result.getInt(1),
				result.getString(2),
				result.getBoolean(4)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
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
					   "WHERE login = '%s' " +
					   "AND password = '%s' ";
		query = String.format(query, login, password);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static User request(int userId) {
		String query = "SELECT * FROM \"USER\" " +
					   "WHERE user_id = %d ";
		query = String.format(query, userId);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static User request(User user) {
		return request(user.getID());
	}

	public static User fromWish(Wish wish) {
		return request(wish.getUserId());
	}

	public static User fromBorrow(Borrow borrow) {
		return request(borrow.getUserId());
	}
}
