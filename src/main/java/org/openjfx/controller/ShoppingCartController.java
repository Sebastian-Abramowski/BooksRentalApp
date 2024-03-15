package org.openjfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ShoppingCartController {
	@FXML
	private TableView<?> tableBooks;
	@FXML
	private TableColumn<?, ?> title;
	@FXML
	private TableColumn<?, ?> bookId;
	@FXML
	private Button btnBuy;
	@FXML
	private Button btnGoBack;
	@FXML
	private TableColumn<?, ?> price;
	@FXML
	private TableColumn<?, Void> removeRow;

	@FXML
	void onBuyClick(ActionEvent event) {
//		System.out.println("Buy clicked");
	}

	@FXML
	void onGoBackClick(ActionEvent event) {
//		System.out.println("Go back clicked");
	}

}
