package org.openjfx.database;

import java.sql.ResultSet;

public class Logger {
	/*
	 * If you can run functions "inline" like VSCode can, here is where you can
	 * execute some arbitrary database code to your liking. Example below.
	 */
	public static void main(String[] args) {
		Database database = Database.getInstance();
		ResultSet result = database.executeQuery("SELECT * FROM BOOK");
		System.out.println(result);
	}
}
