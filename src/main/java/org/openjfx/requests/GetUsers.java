package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Borrow;
import org.openjfx.database.User;
import org.openjfx.database.Wish;

public class GetUsers extends Request {
	public static ArrayList<User> fromResult(ResultSet result) {
		ArrayList<User> users = new ArrayList<User>();
		User user;
		while((user = GetUser.fromResult(result)) != null) {
			users.add(user);
		}
		return users;
	}

	public static ArrayList<User> request() {
		String query = "SELECT * FROM \"USER\" ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<User> request(String search) {
		String query = "SELECT * FROM \"USER\" " +
					   "WHERE DIFFERENCE(login, '%s') = 4 " +
					   "ORDER BY login ASC";
		query = String.format(query, search);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<User> fromWishes(ArrayList<Wish> wishes) {
		ArrayList<User> users = new ArrayList<User>();
		for(Wish wish : wishes) {
			User user = GetUser.fromWish(wish);
			users.add(user);
		}
		return users;
	}

	public static ArrayList<User> fromBorrows(ArrayList<Borrow> borrows) {
		ArrayList<User> users = new ArrayList<User>();
		for(Borrow borrow : borrows) {
			User user = GetUser.fromBorrow(borrow);
			users.add(user);
		}
		return users;
	}
}
