package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Book;
import org.openjfx.database.ErrorHandler;

public class GetBookInstanceCount extends Request {
	public static int fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return 0;
			}
			return result.getInt(1);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return 0;
		}
	}

	public static int request(Book book) {
		String query = "SELECT COUNT(*) FROM BOOK_INSTANCE " +
					   "WHERE book_id = %d ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}

