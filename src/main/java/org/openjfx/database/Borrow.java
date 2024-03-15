package org.openjfx.database;

import org.openjfx.helpers.Searchable;
import org.openjfx.requests.GetBook;
import org.openjfx.requests.GetUser;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

public class Borrow implements Searchable {
	private int id;
	private int userId;
	private int bookId;
	private int days;
	private LocalDate borrowDate;
	private Boolean acknowledged;

	public Borrow(int id, int userId, int bookId, int days, LocalDate borrowDate, Boolean acknowledged) {
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.days = days;
		this.borrowDate = borrowDate;
		this.acknowledged = acknowledged;
	}

	public Borrow(int id, int userId, int bookId, int days, Date borrowDate, Boolean acknowledged) {
		this(id, userId, bookId, days, borrowDate.toLocalDate(), acknowledged);
	}

	public int getId() { return id; }
	public int getUserId() { return userId; }
	public int getBookId() { return bookId; }
	public int getDays() { return days; }
	public LocalDate getBorrowDate() { return borrowDate; }
	public LocalDate getReturnDate() { return borrowDate.plusDays(days); }
	public Boolean getAcknowledged() { return acknowledged; }

	public int isBorrowLate(){
		return getReturnDate().isAfter( LocalDate.now() ) ? 1 : 0;
	}

	public List<String> getSearchParams() {
		var book = GetBook.request(bookId);
		var user = GetUser.request(userId);
		return Arrays.asList ( book.getAuthor(), book.getCategory(), book.getTitle(), borrowDate.toString(), user.getLogin());
	}

}
