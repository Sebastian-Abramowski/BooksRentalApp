package org.openjfx.requests;

import java.util.ArrayList;
import java.util.List;

import org.openjfx.database.User;

public class GetTabs extends Request {
	public static List<TabData> request(User usr) {

		var list = new ArrayList<TabData>();

		if (usr.isAdmin())
		{
			list.add(new TabData("All books", "AdminView_AllBooks"));
			list.add(new TabData("Borrowed books", "AdminView_BorrowedBooks"));
			list.add(new TabData("Wanted to be borrowed", "AdminView_WantedBooks"));
			list.add(new TabData("Add book", "AdminView_AddBook"));
			list.add(new TabData("Notifications", "AdminView_Notifications"));
		}
		else {
			list.add(new TabData("Borrowed books", "UserView_BorrowedBooks"));
			list.add(new TabData("Available books", "UserView_AvailableBooks"));
			list.add(new TabData("Wished books", "UserView_WishedBooks"));
			list.add(new TabData("Notifications", "UserView_Notifications"));
		}

		return list;
	}

	public static class TabData {

		private String tabName;
		private String viewFileName;


		public TabData(String tabName, String viewFileName) {
			this.tabName = tabName;
			this.viewFileName = viewFileName;
		}

		public String getTabName() { return tabName; }
		public String getViewFileName() { return viewFileName; }
 	}
}
