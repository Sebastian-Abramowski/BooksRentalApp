package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetBook;
import org.openjfx.requests.GetBookInstanceCount;
import org.openjfx.requests.GetClient;

public class Wish implements Searchable {
	private int id;
	private int book_id;
	private int days;
	private LocalDate date;
	private int client_id;

	/* DO NOT USE DIRECTLY! */
	private Book book = null;
	private Client client = null;

	public Wish(int id, int book_id, int days, LocalDate date, int client_id) {
		this.id = id;
		this.book_id = book_id;
		this.days = days;
		this.date = date;
		this.client_id = client_id;
	}

	public Wish(int id, Book book, int days, LocalDate date, int client_id) {
		this.id = id;
		this.book_id = book.getId();
		this.days = days;
		this.date = date;
		this.client_id = client_id;

		this.book = book;
	}

	public Wish(int id, int book_id, int days, LocalDate date, Client client) {
		this.id = id;
		this.book_id = book_id;
		this.days = days;
		this.date = date;
		this.client_id = client.getId();

		this.client = client;
	}

	public Wish(int id, Book book, int days, LocalDate date, Client client) {
		this.id = id;
		this.book_id = book.getId();
		this.days = days;
		this.date = date;
		this.client_id = client.getId();

		this.book = book;
		this.client = client;
	}

	public int getId() { return id; }
	public int getBookId() { return book_id; }
	public int getDays() { return days; }
	public LocalDate getDate() { return date; }
	public int getClientId() { return client_id; }

	public Book getBook() {
		if(book == null) {
			book = GetBook.request(book_id);
		}
		return book;
	}

	public Client getClient() {
		if(client == null) {
			client = GetClient.request(client_id);
		}
		return client;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getBook().getSearchParams());
		searchParams.add(String.valueOf(days));
		searchParams.add(date.toString());
		searchParams.addAll(getClient().getSearchParams());
		return searchParams;
	}

	public int isWishDoable() {
		Book book = getBook();
		int amount = GetBookInstanceCount.request(book);
		return amount > 0 ? 1 : 0;
	}
}
