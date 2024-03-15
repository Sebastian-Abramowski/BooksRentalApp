package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.User;
import org.openjfx.database.Book;
import org.openjfx.database.Wish;
import org.openjfx.database.ErrorHandler;

public class GetWish extends Request {
	public static Wish fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new Wish(
				result.getInt(1),
				result.getInt(2),
				result.getInt(3),
				result.getInt(4)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Wish request(User user, Book book) {
		String query = "SELECT * FROM WISH " +
					   "WHERE user_id = %d " +
					   "AND book_id = %d ";
		query = String.format(query, user.getID(), book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Wish request(int wishId) {
		String query = "SELECT * FROM WISH " +
					   "WHERE wish_id = %d ";
		query = String.format(query, wishId);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Wish request(Wish wish) {
		return request(wish.getId());
	}

	public static Wish request(Book book) {
		String query = "SELECT * FROM WISH " +
				"WHERE book_id = %d ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}


