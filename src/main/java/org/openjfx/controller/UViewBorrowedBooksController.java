package org.openjfx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.Arrays;
import java.util.List;

import org.openjfx.database.Borrow;
import org.openjfx.helpers.Filter;
import org.openjfx.helpers.Searchable;
import org.openjfx.requests.GetBook;
import org.openjfx.requests.GetUserBorrows;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class UViewBorrowedBooksController implements Initializable {

	private class DisplayRecord implements Searchable
	{
		public ObjectProperty<String> title;
		public ObjectProperty<String> author;
		public ObjectProperty<String> category;
		public ObjectProperty<Boolean> wasPickedUp;
		public ObjectProperty<LocalDate> borrowDate;
		public ObjectProperty<Integer> daysReminded;

		public DisplayRecord(Borrow borrow) {
			var book = GetBook.request(borrow.getBookId());
			title = new SimpleObjectProperty<>(book.getTitle());
			author = new SimpleObjectProperty<>(book.getAuthor());
			category = new SimpleObjectProperty<>(book.getCategory());
			wasPickedUp = new SimpleObjectProperty<>(borrow.getAcknowledged());
			borrowDate = new SimpleObjectProperty<>(borrow.getBorrowDate());
			daysReminded = new SimpleObjectProperty<>((int)ChronoUnit.DAYS.between(LocalDate.now(), borrow.getReturnDate()));
		}

		public List<String> getSearchParams() {
			return Arrays.asList
			(
				title.getValue(),
				author.getValue(),
				category.getValue(),
				borrowDate.getValue().toString()
			);
		}

		public int rowStyleHelper(){
			if(daysReminded.get() <= 0)
				return 0;
			 else if (daysReminded.get() <= 5)
				return 2;
			else
				return 1;
		}
	}


	@FXML
	private VBox vbox;
	@FXML
	private TextField txtSearch;
	@FXML
	private TableView<DisplayRecord> tableBooks;
	@FXML
	private TableColumn<DisplayRecord, String> title;
	@FXML
	private TableColumn<DisplayRecord, String> author;
	@FXML
	private TableColumn<DisplayRecord, String> category;
	@FXML
	private TableColumn<DisplayRecord, Boolean> wasPickedUp;
	@FXML
	private TableColumn<DisplayRecord, LocalDate> borrowDate;
	@FXML
	private TableColumn<DisplayRecord, Integer> daysReminded;

	private ObservableList<DisplayRecord> borrowsList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		title.setCellValueFactory(cellData -> cellData.getValue().title);
		author.setCellValueFactory(cellData -> cellData.getValue().author);
		category.setCellValueFactory(cellData -> cellData.getValue().category);
		wasPickedUp.setCellValueFactory(cellData -> cellData.getValue().wasPickedUp);
		borrowDate.setCellValueFactory(cellData -> cellData.getValue().borrowDate);
		daysReminded.setCellValueFactory(cellData -> cellData.getValue().daysReminded);

		tableBooks.setItems(borrowsList);

		Utils.RowStyler.styleRows(tableBooks, DisplayRecord::rowStyleHelper);
		refreshList();
	}

	@FXML
	void onSearchClick(ActionEvent event) {
		refreshList(txtSearch.getText().toLowerCase());
		txtSearch.clear();
		vbox.requestFocus(); // take away focus from txtSearch TextField
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		refreshList();
	}


	private void refreshList() {
		borrowsList.clear();
		var borrows = GetUserBorrows.request(SceneController.getCurrentUser());
		for (var borow : borrows) {
			borrowsList.add(new DisplayRecord(borow));
		}
	}

	private void refreshList(String key) {
		refreshList();
		if (!key.isEmpty())
			borrowsList.setAll(Filter.match(borrowsList, key));
	}
}
