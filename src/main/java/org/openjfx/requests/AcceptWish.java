package org.openjfx.requests;

import org.openjfx.database.Book;
import org.openjfx.database.Borrow;
import org.openjfx.database.Wish;

public class AcceptWish {
	public static Borrow request(Wish wish) {
		Book book = GetBook.request(wish.getBookId());
		ChangeBookAmount.request(book, -1);

		DelWish.request(wish);
		return AddBorrow.request(
			GetUser.request(wish.getUserId()),
			GetBook.request(wish.getBookId()),
			wish.getDays()
		);
	}
}
