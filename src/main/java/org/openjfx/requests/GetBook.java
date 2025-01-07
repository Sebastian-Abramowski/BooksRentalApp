package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Book;
import org.openjfx.database.ErrorHandler;

public class GetBook extends Request {
	public static Book fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Book(
				result.getInt(1),
				result.getString(2),
				result.getString(3),
				result.getInt(4),
				result.getInt(5)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Book request(int book_id) {
		String query = "SELECT * FROM BOOK " +
					   "WHERE id = %d ";
		query = String.format(query, book_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);

	}

	public static Book request(String isbn) {
		String query = "SELECT * FROM BOOK " +
					   "WHERE isbn = '%s' ";
		query = String.format(query, isbn);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Book request(Book book) {
		return request(book.getId());
	}
}
