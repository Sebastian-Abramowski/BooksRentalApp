package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.BookInstance;
import org.openjfx.database.Borrow;
import org.openjfx.database.Book;

public class GetBookInstances extends Request {
	public static ArrayList<BookInstance> fromResult(ResultSet result) {
		ArrayList<BookInstance> book_instances = new ArrayList<>();
		BookInstance book_instance;
		while((book_instance = GetBookInstance.fromResult(result)) != null) {
			book_instances.add(book_instance);
		}
		return book_instances;
	}

	public static ArrayList<BookInstance> request() {
		String query = "SELECT * FROM BOOK_INSTANCE ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<BookInstance> request(Book book) {
		String query = "SELECT * FROM BOOK_INSTANCE " +
					   "WHERE book_id = %d ";
		query = String.format(query, book.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<BookInstance> fromBorrows(ArrayList<Borrow> borrows) {
		ArrayList<BookInstance> book_instances = new ArrayList<>();
		for(Borrow borrow : borrows) {
			book_instances.add(borrow.getBookInstance());
		}
		return book_instances;
	}
}
