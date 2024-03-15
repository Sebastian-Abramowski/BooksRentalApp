package org.openjfx.requests;

import java.sql.ResultSet;

import org.openjfx.database.Database;

public abstract class Request {
	protected static ResultSet executeRequest(String query) {
		Database database = Database.getInstance();
		return database.executeQuery(query);
	}

	protected static int executeUpdate(String query) {
		Database database = Database.getInstance();
		return database.executeUpdate(query);
	}
}
