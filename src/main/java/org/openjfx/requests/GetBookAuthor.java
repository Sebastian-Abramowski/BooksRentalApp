package org.openjfx.requests;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.openjfx.database.Book;
import org.openjfx.database.Person;
import org.openjfx.database.BookAuthor;
import org.openjfx.database.ErrorHandler;

public class GetBookAuthor extends Request {
	public static BookAuthor fromResult(ResultSet result) {
		try {
			if(!result.next()) {
				return null;
			}
			return new BookAuthor(
				result.getInt(1),
				result.getInt(2)
			);
		} catch(SQLException e) {
			new ErrorHandler(e);
			return null;
		}
	}

	public static BookAuthor request(int book_id, int person_id) {
		String query = "SELECT * FROM BOOK_AUTHOR " +
					   "WHERE book_id = %d AND person_id = %d ";
		query = String.format(query, book_id, person_id);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static BookAuthor request(Book book, int person_id) {
		return request(book.getId(), person_id);
	}

	public static BookAuthor request(int book_id, Person person) {
		return request(book_id, person.getId());
	}

	public static BookAuthor request(Book book, Person person) {
		return request(book.getId(), person.getId());
	}

	public static BookAuthor request(BookAuthor book_author) {
		return request(book_author.getBookId(), book_author.getPersonId());
	}
}
