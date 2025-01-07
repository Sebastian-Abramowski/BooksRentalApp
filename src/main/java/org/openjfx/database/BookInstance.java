package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetBook;

public class BookInstance implements Searchable {
	private int id;
	private int book_id;

	/* DO NOT USE DIRECTLY! */
	private Book book = null;

	public BookInstance(int id, int book_id) {
		this.id = id;
		this.book_id = book_id;
	}

	public BookInstance(int id, Book book) {
		this.id = id;
		this.book_id = book.getId();

		this.book = book;
	}

	public int getId() { return id; }
	public int getBookId() { return book_id; }

	public Book getBook() {
		if(book == null) {
			book = GetBook.request(book_id);
		}
		return book;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getBook().getSearchParams());
		return searchParams;
	}
}

