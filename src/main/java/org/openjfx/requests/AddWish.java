package org.openjfx.requests;

import org.openjfx.database.Client;
import org.openjfx.database.Book;
import org.openjfx.database.Wish;

public class AddWish extends Request {
	public static Wish request(Client client, Book book, int days) {
		String query = "INSERT INTO WISH (book_id, days, client_id) " +
					   "VALUES (%d, %d, %d) ";
		query = String.format(query, book.getId(), days, client.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetWish.request(client, book);
		} else {
			return null;
		}
	}
}
