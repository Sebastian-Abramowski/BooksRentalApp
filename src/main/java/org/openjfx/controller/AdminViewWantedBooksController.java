package org.openjfx.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

import org.openjfx.database.Log;
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
		userLogin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(GetUser.request(cellData.getValue().getClientId()).getLogin()));
		bookName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(GetBook.request(cellData.getValue().getBookId()).getTitle()));
		days.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDays()));

		tableBooks.setItems(books);
		refreshList();
		this.addButtonsToTableView();
		Utils.RowStyler.styleRows(tableBooks, Wish::isWishDoable);
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
		var logs = GetLogs.request();
		printLogs( logs );
		refreshList();
	}

	private static void printLogs(ArrayList<Log> logs) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (var log: logs){
			String operation = log.getOperationType();
			String date = sdf.format(log.getDate());
			String table = log.getTableName();
			System.out.printf( "%s %s %s\n", operation, date, table);}
	}

	private void addButtonsToTableView() {
		acceptBook.setCellFactory(Utils.createButtonInsideTableColumn("Accept", wish -> acknowledgeBook(wish)));
	}

	private void acknowledgeBook(Wish wish) {
		vbox.requestFocus(); // take away focus
		var admin = GetAdmin.request(SceneController.getCurrentUser());
		if (admin == null)
		{
			System.out.println("Admin is not logged");
			return;
		}

		var borrow = AcceptWish.request(wish, admin);
		if (borrow == null)
		{
			System.out.println("Error occurred");
			return;
		}

		refreshList();
	}
}
