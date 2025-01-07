package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import org.openjfx.database.Book;
import org.openjfx.database.Wish;
import org.openjfx.database.Category;
import org.openjfx.database.BookAuthor;
import org.openjfx.database.BookInstance;

public class GetBooks extends Request {
	public static ArrayList<Book> fromResult(ResultSet result) {
		ArrayList<Book> books = new ArrayList<>();
		Book book;
		while((book = GetBook.fromResult(result)) != null) {
			books.add(book);
		}
		return books;
	}

	public static ArrayList<Book> request() {
		String query = "SELECT * FROM BOOK ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Book> request(Category category) {
		String query = "SELECT * FROM BOOK " +
					   "WHERE category_id = %d ";
		query = String.format(query, category.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Book> fromWishes(ArrayList<Wish> wishes) {
		HashSet<Book> book_set = new HashSet<>();
		ArrayList<Book> books = new ArrayList<>();
		for(Wish wish : wishes) {
			Book book = wish.getBook();
			if(book_set.add(book)) {
				books.add(book);
			}
		}
		return books;
	}

	public static ArrayList<Book> fromBookAuthors(ArrayList<BookAuthor> book_authors) {
		HashSet<Book> book_set = new HashSet<>();
		ArrayList<Book> books = new ArrayList<>();
		for(BookAuthor book_author : book_authors) {
			Book book = book_author.getBook();
			if(book_set.add(book)) {
				books.add(book);
			}
		}
		return books;
	}

	public static ArrayList<Book> fromBookInstances(ArrayList<BookInstance> book_instances) {
		HashSet<Book> book_set = new HashSet<>();
		ArrayList<Book> books = new ArrayList<>();
		for(BookInstance book_instance : book_instances) {
			Book book = book_instance.getBook();
			if(book_set.add(book)) {
				books.add(book);
			}
		}
		return books;
	}
}
