package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.ActiveBorrow;
import org.openjfx.database.Borrow;
import org.openjfx.database.ErrorHandler;

public class GetActiveBorrow extends Request {
	public static ActiveBorrow fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new ActiveBorrow(
				result.getInt(1)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static ActiveBorrow request(int borrow_id) {
		String query = "SELECT * FROM ACTIVE_BORROW " +
					   "WHERE id = %d ";
		query = String.format(query, borrow_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ActiveBorrow request(ActiveBorrow active_borrow) {
		return request(active_borrow.getId());
	}

	public static ActiveBorrow request(Borrow borrow) {
		return request(borrow.getId());
	}
}
