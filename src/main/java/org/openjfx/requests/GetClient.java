package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Client;
import org.openjfx.database.User;
import org.openjfx.database.ErrorHandler;

public class GetClient extends Request {
	public static Client fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Client(
				result.getInt(1),
				result.getString(2)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Client request(int user_id) {
		String query = "SELECT * FROM CLIENT " +
					   "WHERE id = %s ";
		query = String.format(query, user_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Client request(Client client) {
		return request(client.getId());
	}

	public static Client request(String phone_number) {
		String query = "SELECT * FROM CLIENT " +
					   "WHERE phone_number = '%s' ";
		query = String.format(query, phone_number);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Client request(User user) {
		return request(user.getId());
	}
}
