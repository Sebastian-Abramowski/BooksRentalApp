package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import org.openjfx.database.Client;
import org.openjfx.database.User;
import org.openjfx.database.Wish;
import org.openjfx.database.Borrow;

public class GetClients extends Request {
	public static ArrayList<Client> fromResult(ResultSet result) {
		ArrayList<Client> clients = new ArrayList<>();
		Client client;
		while((client = GetClient.fromResult(result)) != null) {
			clients.add(client);
		}
		return clients;
	}

	public static ArrayList<Client> request() {
		String query = "SELECT * FROM CLIENT ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Client> fromUsers(ArrayList<User> users) {
		ArrayList<Client> clients = new ArrayList<Client>();
		for(User user : users) {
			Client client = GetClient.request(user);
			if(client != null) {
				clients.add(client);
			}
		}
		return clients;
	}

	public static ArrayList<Client> fromWishes(ArrayList<Wish> wishes) {
		HashSet<Client> client_set = new HashSet<>();
		ArrayList<Client> clients = new ArrayList<>();
		for(Wish wish : wishes) {
			Client client = wish.getClient();
			if(client_set.add(client)) {
				clients.add(client);
			}
		}
		return clients;
	}

	public static ArrayList<Client> fromBorrows(ArrayList<Borrow> borrows) {
		HashSet<Client> client_set = new HashSet<>();
		ArrayList<Client> clients = new ArrayList<>();
		for(Borrow borrow : borrows) {
			Client client = borrow.getClient();
			if(client_set.add(client)) {
				clients.add(client);
			}
		}
		return clients;
	}
}
