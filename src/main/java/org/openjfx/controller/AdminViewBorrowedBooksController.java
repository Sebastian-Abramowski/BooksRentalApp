package org.openjfx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import org.openjfx.database.Borrow;
import org.openjfx.helpers.Filter;
import org.openjfx.requests.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class AdminViewBorrowedBooksController implements Initializable {
	@FXML
	private VBox vbox;
	@FXML
	private TextField txtSearch;
	@FXML
	private TableColumn<Borrow, String> userLogin;
	@FXML
	private TableColumn<Borrow, String> bookName;
	@FXML
	private TableColumn<Borrow, Integer> days;
	@FXML
	private TableColumn<Borrow, LocalDate> date;
	@FXML
	private TableColumn<Borrow, Boolean> acknowledge;
	@FXML
	private TableColumn<Borrow, Void> returnBook;
	@FXML
	private TableColumn<Borrow, Void> collectBook;
	@FXML
	private TableView<Borrow> tableBooks;

	private ObservableList<Borrow> borrowList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		userLogin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUserLogin()));
		bookName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTitle()));
		days.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDays()));
		date.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
		acknowledge.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAcknowledged()));

		tableBooks.setItems( borrowList );
		refreshList();

		this.addButtonsToTableView();

		Utils.RowStyler.styleRows(tableBooks,  Borrow::isBorrowNotLate);
		Utils.sortTableView(tableBooks, date, TableColumn.SortType.ASCENDING);
	}


	private void refreshList() {
		borrowList.clear();

		var bookArrayList = GetBorrows.fromActiveBorrows( GetActiveBorrows.request() );
		if (bookArrayList != null) {
			borrowList.addAll(bookArrayList);
		}
	}

	private void refreshList(String key) {
		refreshList();
		borrowList.setAll( Filter.match( borrowList, key));
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		this.refreshList();
		Utils.sortTableView(tableBooks, date, TableColumn.SortType.ASCENDING);
		PrintBorrowStats.print();
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
		returnBook.setCellFactory(Utils.createButtonInsideTableColumn("Returned", book -> removeBook(book)));
		collectBook.setCellFactory(Utils.createButtonInsideTableColumn("Collected", book -> markAsCollected(book)));
	}

	private void removeBook(Borrow borrow) {
		var admin = GetAdmin.request(SceneController.getCurrentUser());
		if (admin == null)
		{
			System.out.println("Admin is not logged");
			return;
		}
		DelActiveBorrow.request(borrow, admin);
		refreshList();

		vbox.requestFocus(); // take away focus
	}

	private void markAsCollected(Borrow borrow) {
		BorrowAcknowledged.request(borrow);
	}
}
