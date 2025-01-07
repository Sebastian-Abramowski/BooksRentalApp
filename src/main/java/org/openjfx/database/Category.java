package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

public class Category implements Searchable {
	private int id;
	private String name;

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() { return id; }
	public String getName() { return name; }

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.add(name);
		return searchParams;
	}

	@Override
	public String toString() {
		return name;
	}
}

