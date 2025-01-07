package org.openjfx.requests;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

import org.openjfx.database.Admin;
import org.openjfx.database.User;
import org.openjfx.database.Borrow;
import org.openjfx.database.ArchivedBorrow;

public class GetAdmins extends Request {
	public static ArrayList<Admin> fromResult(ResultSet result) {
		ArrayList<Admin> admins = new ArrayList<>();
		Admin admin;
		while((admin = GetAdmin.fromResult(result)) != null) {
			admins.add(admin);
		}
		return admins;
	}

	public static ArrayList<Admin> request() {
		String query = "SELECT * FROM ADMIN ";
		ResultSet result = executeRequest(query);
		return fromResult(result);
	}

	public static ArrayList<Admin> fromUsers(ArrayList<User> users) {
		ArrayList<Admin> admins = new ArrayList<>();
		for(User user : users) {
			Admin admin = GetAdmin.request(user);
			if(admin != null) {
				admins.add(admin);
			}
		}
		return admins;
	}

	public static ArrayList<Admin> fromBorrows(ArrayList<Borrow> borrows) {
		HashSet<Admin> admin_set = new HashSet<>();
		ArrayList<Admin> admins = new ArrayList<>();
		for(Borrow borrow : borrows) {
			Admin admin = borrow.getApprover();
			if(admin_set.add(admin)) {
				admins.add(admin);
			}
		}
		return admins;
	}

	public static ArrayList<Admin> fromArchivedBorrows(ArrayList<ArchivedBorrow> archived_borrows) {
		HashSet<Admin> admin_set = new HashSet<>();
		ArrayList<Admin> admins = new ArrayList<>();
		for(ArchivedBorrow archived_borrow : archived_borrows) {
			Admin admin = archived_borrow.getApprover();
			if(admin_set.add(admin)) {
				admins.add(admin);
			}
		}
		return admins;
	}
}
