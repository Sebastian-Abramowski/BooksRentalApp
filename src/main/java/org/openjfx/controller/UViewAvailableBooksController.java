package org.openjfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.openjfx.database.Book;
import org.openjfx.requests.*;
import org.openjfx.helpers.Filter;


import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UViewAvailableBooksController implements Initializable {
	@FXML
	private VBox vbox;
	@FXML
	private TextField txtSearch;
	@FXML
	private TextField txtDaysRequest;
	@FXML
	private Label labelErrors;
	@FXML
	private TableView<Book> tableBooks;
	@FXML
	private TableColumn<Book, String> title;
	@FXML
	private TableColumn<Book, String> author;
	@FXML
	private TableColumn<Book, String> category;
	@FXML
	private TableColumn<Book, Integer> rating;
	@FXML
	private TableColumn<Book, Integer> amount;
	@FXML
	private TableColumn<Book, Void> request;

	private ObservableList<Book> booksList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		title.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTitle()));
		author.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAuthor()));
		category.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory()));
		rating.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRating()));
		amount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));

		request.setCellFactory(Utils.createButtonInsideTableColumn("request", book -> requestBook(book)));

		tableBooks.setItems(booksList);
		refreshList();
	}

	@FXML
	private void onRefreshClick(ActionEvent event) {
		this.refreshList();
	}

	@FXML
	private void onSearchClick(ActionEvent event) {
		refreshList(txtSearch.getText().toLowerCase());
		txtSearch.clear();
		vbox.requestFocus(); // take away focus from txtSearch TextField
	}

	@FXML
	private void onDaysRequestChanged() {
		var newValue = txtDaysRequest.getText();
		if (!newValue.matches("\\d*")) {
			newValue = newValue.replaceAll("[^\\d]", "");
            txtDaysRequest.setText(newValue);
        }
		if (!newValue.isEmpty()) {
			labelErrors.setText("");
		}
	}

	private void refreshList() {
		booksList.clear();
		var bookArrayList = GetBooks.request();
		if (bookArrayList != null) {
			booksList.addAll(bookArrayList);
		}
	}

	private void refreshList(String key) {
		refreshList();
		if (!key.isEmpty())
			booksList.setAll(Filter.match(booksList, key));
	}

	private void requestBook(Book book) {
		try {
			var days = Integer.parseInt(txtDaysRequest.getText());
			if (days > 60){
				System.out.println("max days is 60");
				return;
			}
			if (GetWishes.request(SceneController.getCurrentUser()).size()  +
					GetBorrows.request(SceneController.getCurrentUser()).size()>4){
				System.out.println("max borrowed books at once is 5!");
				return;
			}
			AddWish.request(SceneController.getCurrentUser(), book, days);
			System.out.println("Wishe made of " + book.getId() + " on " + days);
			UserViewMainController.instance.selectTab("Wished books");
		}
		catch (NumberFormatException e) {
			labelErrors.setText("Days is not set");
			System.out.println("Days in not an number!");
		}

		vbox.requestFocus(); // take away focus
	}
}
