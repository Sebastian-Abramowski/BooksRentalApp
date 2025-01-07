package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.openjfx.database.ArchivedBorrow;
import org.openjfx.database.Borrow;
import org.openjfx.database.Admin;

public class GetArchivedBorrows extends Request {
	public static ArrayList<ArchivedBorrow> fromResult(ResultSet result) {
		ArrayList<ArchivedBorrow> archived_borrows = new ArrayList<>();
		ArchivedBorrow archived_borrow;
		while((archived_borrow = GetArchivedBorrow.fromResult(result)) != null) {
			archived_borrows.add(archived_borrow);
		}
		return archived_borrows;
	}

	public static ArrayList<ArchivedBorrow> request() {
		String query = "SELECT * FROM ARCHIVED_BORROW ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<ArchivedBorrow> request(Admin admin) {
		String query = "SELECT * FROM ARCHIVED_BORROW " +
					   "WHERE approver_id = %d ";
		query = String.format(query, admin.getId());
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<ArchivedBorrow> fromBorrows(ArrayList<Borrow> borrows) {
		ArrayList<ArchivedBorrow> archived_borrows = new ArrayList<>();
		for(Borrow borrow : borrows) {
			ArchivedBorrow archived_borrow = GetArchivedBorrow.request(borrow);
			if(archived_borrow != null) {
				archived_borrows.add(archived_borrow);
			}
		}
		return archived_borrows;
	}
}
