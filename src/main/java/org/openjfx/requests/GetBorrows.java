package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.Borrow;
import org.openjfx.database.Client;
import org.openjfx.database.Admin;
import org.openjfx.database.BookInstance;
import org.openjfx.database.ActiveBorrow;
import org.openjfx.database.ArchivedBorrow;

public class GetBorrows extends Request {
	public static ArrayList<Borrow> fromResult(ResultSet result) {
		ArrayList<Borrow> borrows = new ArrayList<>();
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

	public static ArrayList<Borrow> request(Client client) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE client_id = %d ";
		query = String.format(query, client.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Borrow> request(Client client, Boolean acknowledged) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE client_id = %d AND acknowledged = %b ";
		query = String.format(query, client.getId(), acknowledged);
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Borrow> request(Admin admin) {
		String query = "SELECT * FROM BORROW " +
					   "WHERE approver_id = %d ";
		query = String.format(query, admin.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Borrow> fromActiveBorrows(ArrayList<ActiveBorrow> active_borrows) {
		ArrayList<Borrow> borrows = new ArrayList<>();
		for(ActiveBorrow active_borrow : active_borrows) {
			borrows.add(active_borrow.getBorrow());
		}
		return borrows;
	}

	public static ArrayList<Borrow> fromActiveBorrows(ArrayList<ActiveBorrow> active_borrows, Client client) {
		ArrayList<Borrow> borrows = new ArrayList<>();
		for(ActiveBorrow active_borrow : active_borrows) {
			var borrow = active_borrow.getBorrow();
			if (borrow.getClient() == client)
				borrows.add(borrow);
		}
		return borrows;
	}

	public static ArrayList<Borrow> fromArchivedBorrows(ArrayList<ArchivedBorrow> archived_borrows) {
		ArrayList<Borrow> borrows = new ArrayList<>();
		for(ArchivedBorrow archived_borrow : archived_borrows) {
			borrows.add(archived_borrow.getBorrow());
		}
		return borrows;
	}

	public static ArrayList<Borrow> fromBookInstances(ArrayList<BookInstance> book_instances) {
		ArrayList<Borrow> borrows = new ArrayList<>();
		for(BookInstance book_instance : book_instances) {
			Borrow borrow = GetBorrow.request(book_instance);
			if(borrow != null) {
				borrows.add(borrow);
			}
		}
		return borrows;
	}
}
