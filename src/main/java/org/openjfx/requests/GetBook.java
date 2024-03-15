package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Book;
import org.openjfx.database.Borrow;
import org.openjfx.database.ErrorHandler;
import org.openjfx.database.Wish;

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
				result.getString(4),
				result.getInt(5),
				result.getInt(6)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Book request(String title, String author) {
		String query = "SELECT * FROM BOOK " +
					   "WHERE title = '%s' " +
					   "AND author = '%s' ";
		query = String.format(query, title, author);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Book request(int bookId) {
		String query = "SELECT * FROM BOOK " +
					   "WHERE book_id = %d ";
		query = String.format(query, bookId);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Book request(Book book) {
		return request(book.getId());
	}

	public static Book fromWish(Wish wish) {
		return request(wish.getBookId());
	}

	public static Book fromBorrow(Borrow borrow) {
		return request(borrow.getBookId());
	}
}


