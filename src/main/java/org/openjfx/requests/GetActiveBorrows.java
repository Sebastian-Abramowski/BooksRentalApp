package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.ActiveBorrow;
import org.openjfx.database.Borrow;
import org.openjfx.database.Client;

public class GetActiveBorrows extends Request {
	public static ArrayList<ActiveBorrow> fromResult(ResultSet result) {
		ArrayList<ActiveBorrow> active_borrows = new ArrayList<>();
		ActiveBorrow active_borrow;
		while((active_borrow = GetActiveBorrow.fromResult(result)) != null) {
			active_borrows.add(active_borrow);
		}
		return active_borrows;
	}

	public static ArrayList<ActiveBorrow> request() {
		String query = "SELECT * FROM ACTIVE_BORROW ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<ActiveBorrow> request(Client client) {
		String query = "SELECT * FROM ACTIVE_BORROW ab " +
					   "JOIN BORROW b ON ab.id = b.id " +
					   "WHERE b.client_id = %d ";
		query = String.format(query, client.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<ActiveBorrow> fromBorrows(ArrayList<Borrow> borrows) {
		ArrayList<ActiveBorrow> active_borrows = new ArrayList<>();
		for(Borrow borrow : borrows) {
			ActiveBorrow active_borrow = GetActiveBorrow.request(borrow);
			if(active_borrow != null) {
				active_borrows.add(active_borrow);
			}
		}
		return active_borrows;
	}
}
