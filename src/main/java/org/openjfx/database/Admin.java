package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetUser;

public class Admin implements Searchable {
	private int id;

	/* DO NOT USE DIRECTLY! */
	private User user = null;

	public Admin(int id) {
		this.id = id;
	}

	public Admin(User user) {
		this.id = user.getId();

		this.user = user;
	}

	public int getId() { return id; }

	public User getUser() {
		if(user == null) {
			user = GetUser.request(id);
		}
		return user;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.addAll(getUser().getSearchParams());
		return searchParams;
	}
}
