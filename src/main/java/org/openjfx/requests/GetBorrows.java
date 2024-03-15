package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Borrow;
import org.openjfx.database.User;

public class GetBorrows extends Request {
	public static ArrayList<Borrow> fromResult(ResultSet result) {
		ArrayList<Borrow> borrows = new ArrayList<Borrow>();
		Borrow borrow;
		while((borrow = GetBorrow.fromResult(result)) != null) {
			borrows.add(borrow);
		}
		return borrows;
	}

	public static ArrayList<Borrow> request() {
		String query = "SELECT * FROM BORROW ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}


	public static ArrayList<Borrow> request(User user) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE user_id = %d ";
		query = String.format(query, user.getID());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}
}
