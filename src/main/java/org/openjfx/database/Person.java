package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

public class Person implements Searchable {
	private int id;
	private String name;
	private String surname;

	public Person(int id, String name, String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public int getId() { return id; }
	public String getName() { return name; }
	public String getSurname() { return surname; }

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.add(name);
		searchParams.add(surname);
		return searchParams;
	}

	@Override
	public String toString() {
		return name + " " + surname;
	}
}

