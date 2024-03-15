package org.openjfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import org.openjfx.database.Book;
import org.openjfx.helpers.Filter;
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
	private TableView<Book> tableBooks;
	@FXML
	private TableColumn<Book, String> title;
	@FXML
	private TableColumn<Book, String> author;
	@FXML
	private TableColumn<Book, String> category;
	@FXML
	private TableColumn<Book, Void> changeableRating;
	@FXML
	private TableColumn<Book, Integer> amount;
	@FXML
	private TableColumn<Book, Void> removeRow;
	@FXML
	private TableColumn<Book, Void> amountAdd;
	@FXML
	private TableColumn<Book, Void> amountSubtract;

	private ObservableList<Book> books = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		title.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTitle()));
		author.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAuthor()));
		category.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory()));
		amount.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAmount()));

		tableBooks.setItems(books);
		refreshList();

		this.addButtonsToTableView();
	}

	private void refreshList() {
		books.clear();
		var bookArrayList = GetBooks.request();
		if (bookArrayList != null) {
			books.addAll(bookArrayList);
		}
	}

	private void refreshList(String key) {
		refreshList();
		books.setAll( Filter.match(books, key));
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		this.refreshList();
	}

	@FXML
	void onSearchClick(ActionEvent event) {
		if (txtSearch.getText().isEmpty())
			return;

		refreshList(txtSearch.getText().toLowerCase());

		txtSearch.clear();
		vbox.requestFocus(); // take away focus from txtSearch TextField
	}

	private void addButtonsToTableView() {
		removeRow.setCellFactory(Utils.createButtonInsideTableColumn("Remove", book -> removeBook(book)));
		amountAdd.setCellFactory(Utils.createButtonInsideTableColumn("+1", book -> addAmount(book)));
		amountSubtract.setCellFactory(Utils.createButtonInsideTableColumn("-1", book -> subtractAmount(book)));
		setSpinnerToRating(1, 5);
	}

	private void removeBook(Book book) {
		if (GetWish.request( book ) != null) {
			System.out.println( "cant remove, book is wished" );
			return;
		}
		try {
			var deleted = DelBook.request(book);
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

	private void addAmount(Book book) {
		ChangeBookAmount.request(book, 1);
		System.out.println(book.getAmount());
		refreshList();

		vbox.requestFocus(); // take away focus
	}

	private void subtractAmount(Book book) {
		if (book.getAmount() > 0) {
			ChangeBookAmount.request(book, -1);
			refreshList();
		}
		else
			System.out.println("cant decrement");

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
							Book book = getTableView().getItems().get(getIndex());
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
		ModifyBookRating.request( newRating, book.getId() );
	}
}
