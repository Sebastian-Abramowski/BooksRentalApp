package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.User;
import org.openjfx.database.Borrow;

public class GetUserBorrows extends Request {
	public static ArrayList<Borrow> request(User user) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE user_id = %d ";
		query = String.format(query, user.getID());
		ResultSet result = executeRequest(query);
		return GetBorrows.fromResult(result);
	}

	public static ArrayList<Borrow> request(User user, Boolean acknowledged) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE user_id = %d " +
					   "AND acknowledged = %b";
		query = String.format(query, user.getID(), acknowledged);
		ResultSet result = executeRequest(query);
		return GetBorrows.fromResult(result);
	}
}
