package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Borrow;
import org.openjfx.database.BookInstance;
import org.openjfx.database.ErrorHandler;

public class GetBorrow extends Request {
	public static Borrow fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Borrow(
				result.getInt(1),
				result.getInt(2),
				result.getInt(3),
				result.getDate(4).toLocalDate(),
				result.getBoolean(5),
				result.getInt(6),
				result.getInt(7)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Borrow request(int borrow_id) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE id = %d ";
		query = String.format(query, borrow_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Borrow request(Borrow borrow) {
		return request(borrow.getId());
	}

	public static Borrow request(BookInstance book_instance) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE book_instance_id = %d ";
		query = String.format(query, book_instance.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}
