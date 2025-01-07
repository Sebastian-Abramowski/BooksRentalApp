package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetBorrow;
import org.openjfx.requests.GetAdmin;

public class ArchivedBorrow implements Searchable {
	private int id;
	private LocalDate date;
	private int approver_id;

	/* DO NOT USE DIRECTLY! */
	private Borrow borrow = null;
	private Admin approver = null;

	public ArchivedBorrow(int borrow_id, LocalDate date, int approver_id) {
		this.id = borrow_id;
		this.date = date;
		this.approver_id = approver_id;
	}

	public ArchivedBorrow(Borrow borrow, LocalDate date, int approver_id) {
		this.id = borrow.getId();
		this.date = date;
		this.approver_id = approver_id;

		this.borrow = borrow;
	}

	public ArchivedBorrow(int borrow_id, LocalDate date, Admin approver) {
		this.id = borrow_id;
		this.date = date;
		this.approver_id = approver.getId();

		this.approver = approver;
	}

	public ArchivedBorrow(Borrow borrow, LocalDate date, Admin approver) {
		this.id = borrow.getId();
		this.date = date;
		this.approver_id = approver.getId();

		this.borrow = borrow;
		this.approver = approver;
	}

	public int getId() { return id; }
	public LocalDate getDate() { return date; }
	public int getApproverId() { return approver_id; }

	public Borrow getBorrow() {
		if(borrow == null) {
			borrow = GetBorrow.request(id);
		}
		return borrow;
	}

	public Admin getApprover() {
		if(approver == null) {
			approver = GetAdmin.request(approver_id);
		}
		return approver;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getBorrow().getSearchParams());
		searchParams.add(date.toString());
		searchParams.addAll(getApprover().getSearchParams());
		return searchParams;
	}
}

