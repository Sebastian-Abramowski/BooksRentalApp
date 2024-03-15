package org.openjfx.controller;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.openjfx.database.Wish;
import org.openjfx.requests.DelWish;
import org.openjfx.requests.GetBook;
import org.openjfx.requests.GetUserWishes;
import org.openjfx.helpers.Filter;
import org.openjfx.helpers.Searchable;

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


public class UViewWishesController implements Initializable {

	private class DisplayRecord implements Searchable
	{
		public ObjectProperty<String> title;
		public ObjectProperty<String> author;
		public ObjectProperty<String> category;
		public ObjectProperty<Integer> rating;
		public ObjectProperty<Integer> forDays;

		private Wish wish;

		public DisplayRecord(Wish wish) {
			this.wish = wish;
			var book = GetBook.request(wish.getBookId());
			title = new SimpleObjectProperty<>(book.getTitle());
			author = new SimpleObjectProperty<>(book.getAuthor());
			category = new SimpleObjectProperty<>(book.getCategory());
			rating = new SimpleObjectProperty<>(book.getRating());
			forDays = new SimpleObjectProperty<>(wish.getDays());
		}

		public List<String> getSearchParams() {
			return Arrays.asList
			(
				title.getValue(),
				author.getValue(),
				category.getValue()
				);
		}

		public Wish getWish() { return wish; }
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
	private TableColumn<DisplayRecord, Integer> rating;
	@FXML
	private TableColumn<DisplayRecord, Integer> forDays;
	@FXML
	private TableColumn<DisplayRecord, Void> request;


	private ObservableList<DisplayRecord> wishedList = FXCollections.observableArrayList();
	private String currenSearchKey = "";

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		title.setCellValueFactory(cellData -> cellData.getValue().title);
		author.setCellValueFactory(cellData -> cellData.getValue().author);
		category.setCellValueFactory(cellData -> cellData.getValue().category);
		rating.setCellValueFactory(cellData -> cellData.getValue().rating);
		forDays.setCellValueFactory(cellData -> cellData.getValue().forDays);

		request.setCellFactory(Utils.createButtonInsideTableColumn("delete", record -> delateWish(record.getWish())));

		tableBooks.setItems(wishedList);
		refreshList();
	}

	@FXML
	void onRefreshClick(ActionEvent event) {
		this.refreshList();
	}

	@FXML
	void onSearchClick(ActionEvent event) {
		currenSearchKey = txtSearch.getText();
		refreshList(currenSearchKey);
		txtSearch.clear();
		vbox.requestFocus(); // take away focus from txtSearch TextField
	}

	private void delateWish(Wish wish) {
		System.out.println("Deleted wish");
		DelWish.request(wish);
		refreshList(currenSearchKey);
		vbox.requestFocus(); // take away focus
	}

	private void refreshList() {
		wishedList.clear();
		var wishes = GetUserWishes.request(SceneController.getCurrentUser());
		System.out.println(wishes.size());
		for (var wishe : wishes) {
			wishedList.add(new DisplayRecord(wishe));
		}
	}

	private void refreshList(String key) {
		refreshList();
		if (!key.isEmpty())
			wishedList.setAll(Filter.match(wishedList, key));
	}
}
