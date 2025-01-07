package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetBook;
import org.openjfx.requests.GetPerson;

public class BookAuthor implements Searchable {
	private int book_id;
	private int person_id;

	/* DO NOT USE DIRECTLY! */
	private Book book = null;
	private Person person = null;

	public BookAuthor(int book_id, int person_id) {
		this.book_id = book_id;
		this.person_id = person_id;
	}

	public BookAuthor(Book book, int person_id) {
		this.book_id = book.getId();
		this.person_id = person_id;

		this.book = book;
	}

	public BookAuthor(int book_id, Person person) {
		this.book_id = book_id;
		this.person_id = person.getId();

		this.person = person;
	}

	public BookAuthor(Book book, Person person) {
		this.book_id = book.getId();
		this.person_id = person.getId();

		this.book = book;
		this.person = person;
	}

	public int getBookId() { return book_id; }
	public int getPersonId() { return person_id; }

	public Book getBook() {
		if(book == null) {
			book = GetBook.request(book_id);
		}
		return book;
	}

	public Person getPerson() {
		if(person == null) {
			person = GetPerson.request(person_id);
		}
		return person;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getBook().getSearchParams());
		searchParams.addAll(getPerson().getSearchParams());
		return searchParams;
	}
}
