package org.openjfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.openjfx.requests.AddPerson;
import org.openjfx.requests.DelPerson;
import org.openjfx.requests.GetPerson;

public class AdminViewAddAuthorController {

	@FXML
	private Label labelInfo;
	@FXML
	private Label labelErrors;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtSurname;


	@FXML
	void onSubmitClick(ActionEvent event) {
		clearLabels();

		try {
			var person = AddPerson.request(txtName.getText(), txtSurname.getText());

			if (person == null)
				labelInfo.setText("Person cannot be added. Name and surname might be not unique.");
			else
				labelInfo.setText("New person was added with name: " + txtName.getText() + " and surname "
						+ txtSurname.getText());

		}
		catch (Exception e) {
			labelErrors.setText("Some error occurred");
		}
	}

	@FXML
	void onRemoveAuthorClick(ActionEvent event) {
		clearLabels();

		try {
			var person = GetPerson.request(txtName.getText(), txtSurname.getText());

			if (person == null) {
				labelInfo.setText("No person with this name and surname was found to be removed");
				return;
			}
			DelPerson.request(person);
			labelInfo.setText("New person was removed with name: " + txtName.getText() + " and surname "
					+ txtSurname.getText());
		}
		catch (Exception e) {
			labelErrors.setText("Some error occurred");
		}
	}



	@FXML
	void onClearClick(ActionEvent event) {
		txtName.clear();
		txtSurname.clear();
		clearLabels();
	}

	private void clearLabels() {
		labelInfo.setText("");
		labelErrors.setText("");
	}
}

