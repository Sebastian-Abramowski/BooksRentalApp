package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.User;
import org.openjfx.database.Book;
import org.openjfx.database.Borrow;
import org.openjfx.database.ErrorHandler;

public class GetBorrow extends Request {
	public static Borrow fromResult(ResultSet result) {
		try {
			if (!result.next()) {
//				System.out.println("ResultSet is empty");
				return null;
			} else {
				// Continue processing the ResultSet
				var id =result.getInt(1);
				var userId = result.getInt(2);
				var bookId = result.getInt(3);
				var BorrowDate = result.getInt(4);
				var LocalDate = result.getDate(5);
				var ack = result.getBoolean(6);
				return new Borrow(id, userId, bookId, BorrowDate, LocalDate, ack);
			}
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Borrow request(User user, Book book) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE user_id = %d " +
					   "AND book_id = %d ";
		query = String.format(query, user.getID(), book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Borrow request(int borrowId) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE borrow_id = %d ";
		query = String.format(query, borrowId);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Borrow request(Borrow borrow) {
		return request(borrow.getId());
	}
}
