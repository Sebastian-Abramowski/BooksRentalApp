package org.openjfx.database;

public class Initializer {
	/*
	 * Do not fricking execute this without making any changes to Database.java's createDatabase().
	 */
	public static void main(String[] args) {
		Database database = Database.getInstance();
		database.printLogs();
		database.createDatabase(true);
	}
}
