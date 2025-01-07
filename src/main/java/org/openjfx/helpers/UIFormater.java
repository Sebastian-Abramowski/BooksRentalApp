package org.openjfx.helpers;

import org.openjfx.database.BookAuthor;

import java.util.ArrayList;

public class UIFormater {

	public static String formatAuthors(ArrayList<BookAuthor> authors) {
		var result = "";
		for (var author : authors){
			if (!result.isEmpty())
				result += "\n";
			var person = author.getPerson();
			result += person.getName() + " " + person.getSurname();
		}
		return result;
	}

}
