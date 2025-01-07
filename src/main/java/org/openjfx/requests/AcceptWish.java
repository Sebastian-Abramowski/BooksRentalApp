package org.openjfx.requests;

import org.openjfx.database.Client;
import org.openjfx.database.Admin;
import org.openjfx.database.Book;
import org.openjfx.database.BookInstance;
import org.openjfx.database.Borrow;
import org.openjfx.database.Wish;

public class AcceptWish {
	public static Borrow request(Wish wish, Admin approver) {
		Book book = wish.getBook();
		BookInstance book_instance = GetFirstBookInstance.request(book);
		if(book_instance == null) {
			return null;
		}
		int days = wish.getDays();
		Client client = wish.getClient();

		DelWish.request(wish);
		return AddBorrow.request(
			book_instance,
			days,
			approver,
			client
		);
	}
}
