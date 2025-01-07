package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.ArchivedBorrow;
import org.openjfx.database.Borrow;
import org.openjfx.database.ErrorHandler;

public class GetArchivedBorrow extends Request {
	public static ArchivedBorrow fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new ArchivedBorrow(
				result.getInt(1),
				result.getDate(2).toLocalDate(),
				result.getInt(3)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static ArchivedBorrow request(int borrow_id) {
		String query = "SELECT * FROM ARCHIVED_BORROW " +
					   "WHERE id = %d ";
		query = String.format(query, borrow_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArchivedBorrow request(ArchivedBorrow archived_borrow) {
		return request(archived_borrow.getId());
	}

	public static ArchivedBorrow request(Borrow borrow) {
		return request(borrow.getId());
	}
}
