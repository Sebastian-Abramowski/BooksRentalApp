package org.openjfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;
import org.openjfx.database.Wish;
import org.openjfx.requests.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class AdminViewWantedBooksController implements Initializable {
	@FXML
	private VBox vbox;
	@FXML
	private TableColumn<Wish, String> userLogin;
	@FXML
	private TableColumn<Wish, String> bookName;
	@FXML
	private TableColumn<Wish, Integer> days;
	@FXML
	private TableColumn<Wish, Void> acceptBook;
	@FXML
	private TableView<Wish> tableBooks;

	private ObservableList<Wish> books = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		userLogin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(GetUser.request(cellData.getValue().getUserId()).getLogin()));
		bookName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(GetBook.request(cellData.getValue().getBookId()).getTitle()));
		days.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDays()));

		tableBooks.setItems(books);
		refreshList();
		this.addButtonsToTableView();
		Utils.RowStyler.styleRows(tableBooks, wishedBook -> wishedBook.isWishDoable());
	}
	private void refreshList() {
		books.clear();
		var bookArrayList = GetWishes.request();
		if (bookArrayList != null) {
			books.addAll(bookArrayList);
		}
	}
	@FXML
	void onRefreshClick(ActionEvent event) {
		refreshList();
	}

	private void addButtonsToTableView() {
		acceptBook.setCellFactory(Utils.createButtonInsideTableColumn("Accept", wish -> acknowledgeBook(wish)));
	}

	private void acknowledgeBook(Wish wish) {
		var book = GetBook.request(wish.getBookId());
		int amount = book.getAmount();
		if (amount > 0) {
			var borrow = AcceptWish.request(wish);
			//TODO some exception handling myb
			refreshList();
		}
		else
			System.out.println("not enough books in store");

		vbox.requestFocus(); // take away focus
	}
}
