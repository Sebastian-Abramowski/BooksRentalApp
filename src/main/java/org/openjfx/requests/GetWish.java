package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Wish;
import org.openjfx.database.Book;
import org.openjfx.database.Client;
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
				result.getDate(4).toLocalDate(),
				result.getInt(5)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static Wish request(int wish_id) {
		String query = "SELECT * FROM WISH " +
					   "WHERE id = %d ";
		query = String.format(query, wish_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Wish request(Wish wish) {
		return request(wish.getId());
	}

	public static Wish request(int client_id, int book_id) {
		String query = "SELECT * FROM WISH " +
					   "WHERE client_id = %d AND book_id = %d ";
		query = String.format(query, client_id, book_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static Wish request(Client client, int book_id) {
		return request(client.getId(), book_id);
	}

	public static Wish request(int client_id, Book book) {
		return request(client_id, book.getId());
	}

	public static Wish request(Client client, Book book) {
		return request(client.getId(), book.getId());
	}
}


