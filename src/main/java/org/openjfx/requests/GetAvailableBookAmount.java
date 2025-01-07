package org.openjfx.requests;

import org.openjfx.database.Book;

public class GetAvailableBookAmount extends Request {
	public static int request(Book book) {
		return GetBookInstanceCount.request(book) - GetBorrowCount.request(book);
	}
}

