package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetUser;

public class Client implements Searchable {
	private int id;
	private String phone_number;

	/* DO NOT USE DIRECTLY! */
	private User user = null;

	public Client(int id, String phone_number) {
		this.id = id;
		this.phone_number = phone_number;
	}

	public Client(User user, String phone_number) {
		this.id = user.getId();
		this.phone_number = phone_number;

		this.user = user;
	}

	public int getId() { return id; };
	public String getPhoneNumber() { return phone_number; }

	public User getUser() {
		if(user == null) {
			user = GetUser.request(id);
		}
		return user;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getUser().getSearchParams());
		searchParams.add(phone_number);
		return searchParams;
	}
}

