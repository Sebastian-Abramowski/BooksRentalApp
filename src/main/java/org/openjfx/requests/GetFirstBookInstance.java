package org.openjfx.requests;

import java.sql.ResultSet;

import org.openjfx.database.Book;
import org.openjfx.database.BookInstance;

public class GetFirstBookInstance extends Request {
	public static BookInstance request(Book book) {
		String query = "SELECT * FROM BOOK_INSTANCE bi " +
					   "LEFT JOIN BORROW b ON bi.id = b.book_instance_id " +
					   "WHERE b.book_instance_id IS NULL AND bi.book_id = %d " +
					   "LIMIT 1 ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return GetBookInstance.fromResult(result);
	}
}
