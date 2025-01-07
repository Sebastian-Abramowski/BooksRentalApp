package org.openjfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import org.openjfx.database.Book;
import org.openjfx.database.BookInstance;
import org.openjfx.helpers.Filter;
import org.openjfx.helpers.UIFormater;
import org.openjfx.requests.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;


public class AdminViewAllBooksController implements Initializable {
	@FXML
	private VBox vbox;
	@FXML
	private TextField txtSearch;
	@FXML
	private TableView<BookInstance> tableBooks;
	@FXML
	private TableColumn<BookInstance, Integer> id;
	@FXML
	private TableColumn<BookInstance, String> title;
	@FXML
	private TableColumn<BookInstance, String> author;
	@FXML
	private TableColumn<BookInstance, String> category;
	@FXML
	private TableColumn<BookInstance, Void> changeableRating;
	@FXML
	private TableColumn<BookInstance, Void> remove;

	private ObservableList<BookInstance> books = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		id.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
		title.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBook().getTitle()));
		author.setCellValueFactory(cellData -> new SimpleObjectProperty<>(UIFormater.formatAuthors(GetBookAuthors.request(cellData.getValue().getBook()))));
		category.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBook().getCategory().getName()));

		tableBooks.setItems(books);
		refreshList();

		this.addButtonsToTableView();
	}

	private void refreshList() {
		books.clear();
		var bookArrayList = GetBookInstances.request();
		if (bookArrayList != null) {
			books.addAll(bookArrayList);
		}

		var key = txtSearch.getText().toLowerCase();
		if (!key.isEmpty()) {
			books.setAll( Filter.match(books, key));
		}
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		this.refreshList();
	}

	@FXML
	void onSearchClick(ActionEvent event) {
		if (txtSearch.getText().isEmpty())
			return;

		refreshList();

		vbox.requestFocus(); // take away focus from txtSearch TextField
	}

	private void addButtonsToTableView() {
		remove.setCellFactory(Utils.createButtonInsideTableColumn("Remove", book -> removeBook(book)));
		setSpinnerToRating(1, 5);
	}

	private void removeBook(BookInstance book) {
		System.out.println("Check if book can be removed");
		try {
			var deleted = DelBookInstance.request(book);
			if (deleted)
				System.out.println("book was removed");
			else
				System.out.println("error with wrong book id");
		}
		catch (Exception e) {
			System.out.println("error with executing sql");
		}
		refreshList();

		vbox.requestFocus(); // take away focus
	}

	private void setSpinnerToRating(int minRating, int maxRating) {
		changeableRating.setCellFactory(
				param -> new TableCell<>() {
					@Override
					protected void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);

						if (empty)
							setGraphic(null);
						else {
							Book book = getTableView().getItems().get(getIndex()).getBook();
							Spinner<Integer> spinner = new Spinner<>(minRating, maxRating, book.getRating());

							spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
								changeRating(book, newValue);
							});

							setGraphic(spinner);
						}
					}
				});
	}

	private void changeRating(Book book, int newRating) {
		System.out.println(book.getTitle() + " this book should have " + newRating + " rating.");
		ModifyBookRating.request( book, newRating );
	}
}
