package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Book;
import org.openjfx.database.Wish;

public class GetBookWishes extends Request {
	public static ArrayList<Wish> request(Book book) {
		String query = "SELECT * FROM WISH " +
					   "WHERE book_id = %d ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return GetWishes.fromResult(result);
	}
}
