package org.openjfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.openjfx.requests.AddCategory;
import org.openjfx.requests.DelCategory;
import org.openjfx.requests.GetCategory;

public class AdminViewAddCategoryController {

	@FXML
	private Label labelInfo;
	@FXML
	private Label labelErrors;
	@FXML
	private TextField txtCategory;


	@FXML
	void onSubmitClick(ActionEvent event) {
		clearLabels();
		try {
			var category = AddCategory.request(txtCategory.getText());
			if (category == null) {
				labelErrors.setText("No category with this name was found to be removed");
				return;
			}
			labelInfo.setText("New category: " + txtCategory.getText() + " was added");
		}
		catch (Exception e) {
			labelErrors.setText("Some error occurred");
		}
	}

	@FXML
	void onRemoveCategoryClick(ActionEvent event) {
		clearLabels();
		try {
			var category = GetCategory.request(txtCategory.getText());
			if (category == null) {
				labelErrors.setText("Empty category cannot be added");
				return;
			}
			DelCategory.request(category);
			labelInfo.setText("Category: " + txtCategory.getText() + " was removed");
		}
		catch (Exception e) {
			labelErrors.setText("Some error occurred");
		}
	}

	@FXML
	void onClearClick(ActionEvent event) {
		txtCategory.clear();
		clearLabels();
	}

	private void clearLabels() {
		labelInfo.setText("");
		labelErrors.setText("");
	}
}

