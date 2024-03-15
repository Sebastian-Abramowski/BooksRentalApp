package org.openjfx.database;

import org.openjfx.requests.GetBook;

public class Wish {
	private int id;
	private int userId;
	private int bookId;
	private int days;

	public Wish(int id, int userId, int bookId, int days)
	{
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.days = days;
	}

	public int getId() { return id; }

	public int getUserId() { return userId; }
	public int getBookId() { return bookId; }

	public int getDays() { return days; }

	public int isWishDoable() {
		return GetBook.fromWish(this).getAmount()>0 ? 1 : 0;
	}
}
