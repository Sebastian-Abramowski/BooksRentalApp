package org.openjfx.requests;

import org.openjfx.database.Client;

public class DelClient extends Request {
	public static Boolean request(Client client) {
		String query = "DELETE FROM CLIENT " +
					   "WHERE id = %d ";
		query = String.format(query, client.getId());
		int result = executeUpdate(query);
		return result == 1;
	}
}
