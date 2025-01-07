package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetPerson;

public class User implements Searchable {
	private int id;
	private String login;
	private String password;
	private int person_id;

	/* DO NOT USE DIRECTLY! */
	private Person person = null;

	public User(int id, String login, String password, int person_id) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.person_id = person_id;
	}

	public User(int id, String login, String password, Person person) {
		this.id = id;
		this.login = login;
		this.password = password;
		this.person_id = person.getId();

		this.person = person;
	}

	public int getId() { return id; }
	public String getLogin() { return login; }
	public String getPassword() { return password; }
	public int getPersonId() { return person_id; }

	public Person getPerson() {
		if(person == null) {
			person = GetPerson.request(person_id);
		}
		return person;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.add(login);
		searchParams.addAll(getPerson().getSearchParams());
		return searchParams;
	}
}

