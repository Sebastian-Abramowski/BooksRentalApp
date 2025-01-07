package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Log;
import org.openjfx.database.ErrorHandler;

public class GetLog extends Request {
	public static Log fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Log(
				result.getInt(1),
				result.getTimestamp(2),
				result.getString(3),
				result.getString(4)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Log request(int log_id) {
		String query = "SELECT * FROM LOG " +
					   "WHERE id = %d ";
		query = String.format(query, log_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Log request(Log log) {
		return request(log.getId());
	}
}
