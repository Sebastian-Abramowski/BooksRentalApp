package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.BookAuthor;
import org.openjfx.database.Book;
import org.openjfx.database.Person;

public class GetBookAuthors extends Request {
	public static ArrayList<BookAuthor> fromResult(ResultSet result) {
		ArrayList<BookAuthor> book_authors = new ArrayList<>();
		BookAuthor book_author;
		while((book_author = GetBookAuthor.fromResult(result)) != null) {
			book_authors.add(book_author);
		}
		return book_authors;
	}

	public static ArrayList<BookAuthor> request() {
		String query = "SELECT * FROM BOOK_AUTHOR ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<BookAuthor> request(Book book) {
		String query = "SELECT * FROM BOOK_AUTHOR " +
					   "WHERE book_id = %d ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<BookAuthor> request(Person person) {
		String query = "SELECT * FROM BOOK_AUTHOR " +
					   "WHERE person_id = %d ";
		query = String.format(query, person.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}
