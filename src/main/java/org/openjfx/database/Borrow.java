package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetBookInstance;
import org.openjfx.requests.GetAdmin;
import org.openjfx.requests.GetClient;

public class Borrow implements Searchable {
	private int id;
	private int book_instance_id;
	private int days;
	private LocalDate date;
	private Boolean acknowledged;
	private int approver_id;
	private int client_id;

	/* DO NOT USE DIRECTLY! */
	private BookInstance book_instance = null;
	private Admin approver = null;
	private Client client = null;

	public Borrow(int id, int book_instance_id, int days, LocalDate date, Boolean acknowledged, int approver_id, int client_id) {
		this.id = id;
		this.book_instance_id = book_instance_id;
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver_id;
		this.client_id = client_id;
	}

	public Borrow(int id, BookInstance book_instance, int days, LocalDate date, Boolean acknowledged, int approver_id, int client_id) {
		this.id = id;
		this.book_instance_id = book_instance.getId();
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver_id;
		this.client_id = client_id;

		this.book_instance = book_instance;
	}

	public Borrow(int id, int book_instance_id, int days, LocalDate date, Boolean acknowledged, Admin approver, int client_id) {
		this.id = id;
		this.book_instance_id = book_instance_id;
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver.getId();
		this.client_id = client_id;

		this.approver = approver;
	}

	public Borrow(int id, int book_instance_id, int days, LocalDate date, Boolean acknowledged, int approver_id, Client client) {
		this.id = id;
		this.book_instance_id = book_instance_id;
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver_id;
		this.client_id = client.getId();

		this.client = client;
	}

	public Borrow(int id, BookInstance book_instance, int days, LocalDate date, Boolean acknowledged, Admin approver, int client_id) {
		this.id = id;
		this.book_instance_id = book_instance.getId();
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver.getId();
		this.client_id = client_id;

		this.book_instance = book_instance;
		this.approver = approver;
	}

	public Borrow(int id, BookInstance book_instance, int days, LocalDate date, Boolean acknowledged, int approver_id, Client client) {
		this.id = id;
		this.book_instance_id = book_instance.getId();
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver_id;
		this.client_id = client.getId();

		this.book_instance = book_instance;
		this.client = client;
	}

	public Borrow(int id, int book_instance_id, int days, LocalDate date, Boolean acknowledged, Admin approver, Client client) {
		this.id = id;
		this.book_instance_id = book_instance_id;
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver.getId();
		this.client_id = client.getId();

		this.approver = approver;
		this.client = client;
	}

	public Borrow(int id, BookInstance book_instance, int days, LocalDate date, Boolean acknowledged, Admin approver, Client client) {
		this.id = id;
		this.book_instance_id = book_instance.getId();
		this.days = days;
		this.date = date;
		this.acknowledged = acknowledged;
		this.approver_id = approver.getId();
		this.client_id = client.getId();

		this.book_instance = book_instance;
		this.approver = approver;
		this.client = client;
	}

	public int getId() { return id; }
	public int getBookInstanceId() { return book_instance_id; }
	public int getDays() { return days; }
	public LocalDate getDate() { return date; }
	public Boolean getAcknowledged() { return acknowledged; }
	public int getApproverId() { return approver_id; }
	public int getClientId() { return client_id; }

	public BookInstance getBookInstance() {
		if(book_instance == null) {
			book_instance = GetBookInstance.request(book_instance_id);
		}
		return book_instance;
	}

	public Admin getApprover() {
		if(approver == null) {
			approver = GetAdmin.request(approver_id);
		}
		return approver;
	}

	public Client getClient() {
		if(client == null) {
			client = GetClient.request(client_id);
		}
		return client;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getBookInstance().getSearchParams());
		searchParams.add(String.valueOf(days));
		searchParams.add(date.toString());
		searchParams.addAll(getApprover().getSearchParams());
		searchParams.addAll(getClient().getSearchParams());
		return searchParams;
	}

	public String getUserLogin() {
		Client client = getClient();
		User user = client.getUser();
		return user.getLogin();
	}

	public String getTitle() {
		BookInstance bookInstance = getBookInstance();
		Book book = bookInstance.getBook();
		return book.getTitle();
	}

	public int isBorrowLate() {
		LocalDate lastProperReturnDate = date.plusDays(days);
		return LocalDate.now().isAfter(lastProperReturnDate) ? 1 : 0;
	}

	public int isBorrowNotLate() {
		return (1 - isBorrowLate());
	}
}

