package org.openjfx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.openjfx.database.Book;
import org.openjfx.requests.*;
import org.openjfx.helpers.Filter;
import org.openjfx.helpers.UIFormater;

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
		author.setCellValueFactory(cellData -> new SimpleObjectProperty<>(UIFormater.formatAuthors(GetBookAuthors.request(cellData.getValue()))));
		category.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory().toString()));
		rating.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getRating()));
		amount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(GetAvailableBookAmount.request(cellData.getValue())));

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
			var client = GetClient.request(SceneController.getCurrentUser());
			if (client == null) {
				System.out.println("Client is not logged");
				return;
			}
			if (GetWishes.request(client).size() + GetActiveBorrows.request(client).size() > 5) {
				System.out.println("Max borrowed (and wished) books at once is 5!");
				return;
			}
//			Now we allow for wishes when amount is not sufficient
//			if(!(GetAvailableBookAmount.request(book) > 0)) {
//				System.out.println("Not enough these books to be borrowed");
//				return;
//			}


			AddWish.request(client, book, days);
			System.out.println("Wish made of " + book.getId() + " on " + LocalDate.now() + " for " + days + " days");
			UserViewMainController.instance.selectTab("Wished books");
		}
		catch (NumberFormatException e) {
			labelErrors.setText("Days is not set");
			System.out.println("Days in not an number!");
		}

		vbox.requestFocus(); // take away focus
	}
}
