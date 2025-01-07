package org.openjfx.database;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.helpers.Searchable;

import org.openjfx.requests.GetCategory;

public class Book implements Searchable {
	private int id;
	private String isbn;
	private String title;
	private int rating;
	private int category_id;

	/* DO NOT USE DIRECTLY! */
	private Category category = null;

	public Book(int id, String isbn, String title, int rating, int category_id) {
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.rating = rating;
		this.category_id = category_id;
	}

	public Book(int id, String isbn, String title, int rating, Category category) {
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.rating = rating;
		this.category_id = category.getId();

		this.category = category;
	}

	public int getId() { return id; }
	public String getIsbn() { return isbn; }
	public String getTitle() { return title; }
	public int getRating() { return rating; }
	public int getCategoryId() { return category_id; }

	public Category getCategory() {
		if(category == null) {
			category = GetCategory.request(category_id);
		}
		return category;
	}

	public List<String> getSearchParams() {
		List<String> searchParams = new ArrayList<>();
		searchParams.add(isbn);
		searchParams.add(title);
		searchParams.add(String.valueOf(rating));
		searchParams.addAll(getCategory().getSearchParams());
		return searchParams;
	}
}

