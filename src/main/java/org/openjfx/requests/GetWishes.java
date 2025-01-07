package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Wish;
import org.openjfx.database.Client;
import org.openjfx.database.Book;

public class GetWishes extends Request {
	public static ArrayList<Wish> fromResult(ResultSet result) {
		ArrayList<Wish> wishes = new ArrayList<>();
		Wish wish;
		while((wish = GetWish.fromResult(result)) != null) {
			wishes.add(wish);
		}
		return wishes;
	}

	public static ArrayList<Wish> request() {
		String query = "SELECT * FROM WISH ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Wish> request(Client client) {
		String query = "SELECT * FROM WISH " +
					   "WHERE client_id = %d ";
		query = String.format(query, client.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Wish> request(Book book) {
		String query = "SELECT * FROM WISH " +
					   "WHERE book_id = %d ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}
