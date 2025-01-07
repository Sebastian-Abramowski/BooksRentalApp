package org.openjfx.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.openjfx.database.Book;
import org.openjfx.requests.GetActiveBorrows;
import org.openjfx.requests.GetClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class UViewNotificationsController implements Initializable {

	@FXML
	private Label notification1;

	@FXML
	private Label notification2;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		refreshNotifications();
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		refreshNotifications();
	}

	void refreshNotifications(){
		setNotification1();
		setNotification2();
	}
	void setNotification1() {
		ArrayList<Book> books = new ArrayList<Book>();
		var client = GetClient.request(SceneController.getCurrentUser());
		if (client == null) {
			System.out.println("Client is not logged");
			return;
		}
		for (var borrow : GetActiveBorrows.request(client)){
			if (!borrow.getBorrow().getAcknowledged() && !borrow.isBorrowLate()) {
				books.add( borrow.getBorrow().getBookInstance().getBook() );
			}
		}

		notification1.setVisible( books.size() != 0 );
		notification1.setManaged( books.size() != 0 );

		if (books.size() == 1) {
			notification1.setText( "Your wish has been accepted, collect \"" + books.get( 0 ).getTitle() + "\"");
		} else if (books.size() > 1) {
			StringBuilder text = new StringBuilder( "Your wishes has been accepted, collect:" );
			for (var book : books) {
				text.append( "\n\"" ).append( book.getTitle() ).append( "\"," );
			}
			text.delete(text.length() - 1, text.length());
			notification1.setText( text.toString() );
		}
	}

	void setNotification2() {
		ArrayList<Book> books = new ArrayList<Book>();
		var client = GetClient.request(SceneController.getCurrentUser());
		if (client == null) {
			System.out.println("Client is not loged");
			return;
		}
		for (var borrow : GetActiveBorrows.request(client)){
			if (borrow.isBorrowLate() ){
				books.add( borrow.getBorrow().getBookInstance().getBook() );
			}
		}
		notification2.setVisible( books.size() != 0 );
		notification2.setManaged( books.size() != 0 );

		if (books.size() == 1) {
			notification2.setText("You are late with book \"" + books.get(0).getTitle() + "\"");
		}
		else {
			StringBuilder text = new StringBuilder( "You are late with books" );
			for (var book: books){
				text.append( "\n\"" ).append( book.getTitle() ).append( "\"" );
				if (book != books.get(books.size()-1))
					text.append( "," );
			}
			notification2.setText(text.toString());
		}
	}

}
