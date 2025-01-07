package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.User;
import org.openjfx.database.Client;
import org.openjfx.database.Admin;

public class GetUsers extends Request {
	public static ArrayList<User> fromResult(ResultSet result) {
		ArrayList<User> users = new ArrayList<>();
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

	public static ArrayList<User> fromClients(ArrayList<Client> clients) {
		ArrayList<User> users = new ArrayList<User>();
		for(Client client : clients) {
			users.add(client.getUser());
		}
		return users;
	}

	public static ArrayList<User> fromAdmins(ArrayList<Admin> admins) {
		ArrayList<User> users = new ArrayList<User>();
		for(Admin admin : admins) {
			users.add(admin.getUser());
		}
		return users;
	}
}
