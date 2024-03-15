package org.openjfx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.openjfx.requests.AddBook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class AdminViewAddBookController implements Initializable {

	@FXML
	private Label labelInfo;
	@FXML
	private Label labelErrors;
	@FXML
	private TextField txtTitle;
	@FXML
	private TextField txtAuthor;
	@FXML
	private TextField txtCategory;
	@FXML
	private Spinner<Integer> amount;

	@FXML
	private ChoiceBox<Integer> ratingChoiceBox;

	private final Integer[] possibleRatings = {1, 2, 3, 4, 5};


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		ratingChoiceBox.getItems().addAll(possibleRatings);
		SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1, 1);
		amount.setValueFactory(valueFactory);
	}

	@FXML
	void onSubmitClick(ActionEvent event) {
		try {
			var book = AddBook.request(
				txtTitle.getText(),
				txtAuthor.getText(),
				txtCategory.getText(),
				ratingChoiceBox.getValue(),
				amount.getValue());
			if (book != null)
				labelInfo.setText(book.getTitle() + " was added...");
			else
				labelErrors.setText("Empty object cannot be added");
		}
		catch (Exception e) {
			labelErrors.setText("Some error occurred");
		}


	}

	@FXML
	void onClearClick(ActionEvent event) {
		txtTitle.clear();
		txtAuthor.clear();
		txtCategory.clear();
		ratingChoiceBox.getSelectionModel().clearSelection();
		labelInfo.setText("");
		labelErrors.setText("");
		amount.getValueFactory().setValue(1);
	}

}
