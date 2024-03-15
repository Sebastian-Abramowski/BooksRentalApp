package org.openjfx.database;

public class Initializer {
	/*
	 * Do not fricking execute this.
	 */
	public static void main(String[] args) {
		Database database = Database.getInstance();
		database.createDatabase(true);
	}
}
