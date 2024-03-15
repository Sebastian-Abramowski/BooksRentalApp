package org.openjfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.openjfx.requests.GetAmount;
import org.openjfx.requests.GetBorrows;
import org.openjfx.requests.GetWishes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class AdminNotificationsController implements Initializable {

	@FXML
	private Label notification1;

	@FXML
	private Label notification2;

	@FXML
	private Label notification3;
	@FXML
	private Label notification4;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		refreshNotifications();
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		refreshNotifications();
	}

	void refreshNotifications(){
		var wishes = GetWishes.request();
		int how_many_wishes = wishes.size();

		var borrowed = GetBorrows.request();
		int how_many_borrowed = borrowed.size();

		var all_amount = GetAmount.request();


		setNotification1(how_many_wishes);
		setNotification2(how_many_borrowed);
		setNotification3(all_amount);

		setNotification4();
	}
	void setNotification1(int how_many_wishes) {
		switch (how_many_wishes) {
			case 0:
				notification1.setText("No books are waiting for borrow acceptation");
				break;
			case 1:
				notification1.setText( how_many_wishes + " book is waiting for borrow acceptation");
				break;
			default:
				notification1.setText( how_many_wishes + " books are waiting for borrow acceptation");
				break;
		}
	}

	void setNotification2(int how_many_borrowed) {
		switch (how_many_borrowed) {
			case 0:
				notification2.setText("No books are waiting currently borrowed");
				break;
			case 1:
				notification2.setText( how_many_borrowed + " book is currently borrowed");
				break;
			default:
				notification2.setText( how_many_borrowed + " books are currently borrowed");
				break;
		}
	}

	void setNotification3(int all_amount) {
		switch (all_amount) {
			case 0:
				notification3.setText("No books are available for borrow");
				break;
			case 1:
				notification3.setText(all_amount + " book is available");
				break;
			default:
				notification3.setText(all_amount + " books are available");
				break;
		}
	}

	void setNotification4(){
		notification4.setVisible( true );
		int all_amount = 0;
		for (var book: GetBorrows.request()){
			if (book.isBorrowLate() == 0){
				all_amount++;
			}
		}
		switch (all_amount) {
			case 0:
				notification4.setVisible( false );
				break;
			case 1:
				notification4.setText(all_amount + " book is late");
				break;
			default:
				notification4.setText(all_amount + " books are late");
				break;
		}
	}
}


