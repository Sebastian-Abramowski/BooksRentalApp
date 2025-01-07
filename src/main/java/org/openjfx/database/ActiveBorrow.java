package org.openjfx.database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetBorrow;

public class ActiveBorrow implements Searchable {
	private int id;

	/* DO NOT USE DIRECTLY! */
	private Borrow borrow = null;

	public ActiveBorrow(int borrow_id) {
		this.id = borrow_id;
	}

	public ActiveBorrow(Borrow borrow) {
		this.id = borrow.getId();

		this.borrow = borrow;
	}

	public int getId() { return id; }

	public Borrow getBorrow() {
		if(borrow == null) {
			borrow = GetBorrow.request(id);
		}
		return borrow;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getBorrow().getSearchParams());
		return searchParams;
	}

	public boolean isBorrowLate() {
		Borrow borrow = getBorrow();
		LocalDate date = borrow.getDate();
		int days = borrow.getDays();
		LocalDate dueDate = date.plusDays(days);
		return LocalDate.now().isAfter(dueDate);
	}
}

