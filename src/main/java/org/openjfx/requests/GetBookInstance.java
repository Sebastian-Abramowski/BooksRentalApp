package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.BookInstance;
import org.openjfx.database.ErrorHandler;

public class GetBookInstance extends Request {
	public static BookInstance fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new BookInstance(
				result.getInt(1),
				result.getInt(2)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static BookInstance request(int instance_id) {
		String query = "SELECT * FROM BOOK_INSTANCE " +
					   "WHERE id = %d ";
		query = String.format(query, instance_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static BookInstance request(BookInstance book_instance) {
		return request(book_instance.getId());
	}
}
