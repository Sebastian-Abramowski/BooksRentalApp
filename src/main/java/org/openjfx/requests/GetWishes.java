package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.User;
import org.openjfx.database.Wish;

public class GetWishes extends Request {
	public static ArrayList<Wish> fromResult(ResultSet result) {
		ArrayList<Wish> wishes = new ArrayList<Wish>();
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

	public static ArrayList<Wish> request(User user) {
		String query = "SELECT * FROM WISH WHERE user_id = " + user.getID();
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
	public static ArrayList<Wish> request(String search) {
		String query = "SELECT w.* FROM (WISH w JOIN BOOK on w.book_id = book_id) " +
					   "WHERE DIFFERENCE(title, '%s') > 2 " +
					   "OR DIFFERENCE(author, '%s') > 2 " +
					   "OR DIFFERENCE(category, '%s') = 4 " +
					   "ORDER BY DIFFERENCE(title, '%s') + DIFFERENCE(author, '%s') + DIFFERENCE(category, '%s') DESC";
		query = String.format(query, search, search, search, search, search, search);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}
