package org.openjfx.requests;

import org.openjfx.database.BookAuthor;
import org.openjfx.database.Book;
import org.openjfx.database.Person;

public class AddBookAuthor extends Request {
	public static BookAuthor request(Book book, Person person) {
		String query = "INSERT INTO BOOK_AUTHOR (book_id, person_id) " +
					   "VALUES (%d, %d) ";
		query = String.format(query, book.getId(), person.getId());
		int result = executeUpdate(query);
		if(result == 1) {
			return GetBookAuthor.request(book, person);
		} else {
			return null;
		}
	}
}
