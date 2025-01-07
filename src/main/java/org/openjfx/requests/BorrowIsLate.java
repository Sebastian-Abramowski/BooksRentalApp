package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Borrow;
import org.openjfx.database.ErrorHandler;

public class BorrowIsLate extends Request {
	public static Boolean fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return result.getInt(1) > 0;
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Boolean request(int borrow_id) {
		String query = "SELECT CASE WHEN DATEADD('DAY', days, date) < NOW() THEN 1 ELSE 0 END " +
					   "FROM BORROW " +
					   "WHERE id = %d";
		query = String.format(query, borrow_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Boolean request(Borrow borrow) {
		return request(borrow.getId());
	}
}
