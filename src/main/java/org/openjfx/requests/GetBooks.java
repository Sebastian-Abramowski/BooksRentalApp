package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Book;
import org.openjfx.database.Borrow;
import org.openjfx.database.Wish;

public class GetBooks extends Request {
	public static ArrayList<Book> fromResult(ResultSet result) {
		ArrayList<Book> books = new ArrayList<Book>();
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


	public static ArrayList<Book> fromWishes(ArrayList<Wish> wishes) {
		ArrayList<Book> books = new ArrayList<Book>();
		for(Wish wish : wishes) {
			Book book = GetBook.fromWish(wish);
			books.add(book);
		}
		return books;
	}

	public static ArrayList<Book> fromBorrows(ArrayList<Borrow> borrows) {
		ArrayList<Book> books = new ArrayList<Book>();
		for(Borrow borrow : borrows) {
			Book book = GetBook.fromBorrow(borrow);
			books.add(book);
		}
		return books;
	}
}
