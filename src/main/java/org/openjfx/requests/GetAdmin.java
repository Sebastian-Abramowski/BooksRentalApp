package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Admin;
import org.openjfx.database.User;
import org.openjfx.database.ErrorHandler;

public class GetAdmin extends Request {
	public static Admin fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Admin(
				result.getInt(1)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Admin request(int user_id) {
		String query = "SELECT * FROM ADMIN " +
					   "WHERE id = %s ";
		query = String.format(query, user_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Admin request(Admin admin) {
		return request(admin.getId());
	}

	public static Admin request(User user) {
		return request(user.getId());
	}
}
