package org.openjfx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.openjfx.database.Book;
import org.openjfx.requests.GetBook;
import org.openjfx.requests.GetBorrows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class UViewNotificationsController implements Initializable {

	@FXML
	private Label notification1;

	@FXML
	private Label notification2;

	@FXML
	private Label notification3;

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
		setNotification3();
	}
	void setNotification1() {
		ArrayList<Book> books = new ArrayList<Book>();
		for (var element: GetBorrows.request(SceneController.getCurrentUser())){
			if (!element.getAcknowledged() && element.getReturnDate().isAfter( LocalDate.now()) ){
				books.add( GetBook.request(element.getBookId()));
			}
		}

		notification1.setVisible( books.size() != 0 );
		notification1.setManaged( books.size() != 0 );

		if (books.size() == 1) {
			notification1.setText( "Your wish/es has been accepted, collect \"" + books.get( 0 ).getTitle() + "\"");
		} else if (books.size() > 1) {
			StringBuilder text = new StringBuilder( "Your wish/es has been accepted, collect:" );
			for (var book : books) {
				text.append( "\n\"" ).append( book.getTitle() ).append( "\"," );
			}
			text.delete(text.length() - 1, text.length());
			notification1.setText( text.toString() );
		}
	}

	void setNotification2() {
		ArrayList<Book> books = new ArrayList<Book>();
		for (var element: GetBorrows.request(SceneController.getCurrentUser())){
			if (!element.getReturnDate().isAfter( LocalDate.now() )){
				books.add( GetBook.request(element.getBookId()));
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

	void setNotification3() {
			notification3.setVisible( false );
			notification3.setManaged( false );
		}
	}


