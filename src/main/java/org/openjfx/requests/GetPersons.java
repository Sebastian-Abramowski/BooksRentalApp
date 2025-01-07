package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import org.openjfx.database.Person;
import org.openjfx.database.BookAuthor;

public class GetPersons extends Request {
	public static ArrayList<Person> fromResult(ResultSet result) {
		ArrayList<Person> persons = new ArrayList<>();
		Person person;
		while((person = GetPerson.fromResult(result)) != null) {
			persons.add(person);
		}
		return persons;
	}

	public static ArrayList<Person> request() {
		String query = "SELECT * FROM PERSON ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Person> fromBookAuthors(ArrayList<BookAuthor> book_authors) {
		HashSet<Person> person_set = new HashSet<>();
		ArrayList<Person> persons = new ArrayList<>();
		for(BookAuthor book_author : book_authors) {
			Person person = book_author.getPerson();
			if(person_set.add(person)) {
				persons.add(person);
			}
		}
		return persons;
	}
}
