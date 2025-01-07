package org.openjfx.controller;

import java.io.IOException;

import javafx.scene.control.PasswordField;
import org.openjfx.database.Client;
import org.openjfx.database.Person;
import org.openjfx.database.User;
import org.openjfx.requests.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class RegisterController {
	@FXML
	private Label labelErrors;
	@FXML
	private Label labelForInfo;
	@FXML
	private TextField txtLogin;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtPhoneNumber;
	@FXML
	private TextField txtSurname;

	private void signaliseIfNoInput() {
		labelForInfo.setText("");
		if (txtLogin.getText().isEmpty()) {
			labelErrors.setText("You need to pass a login");
		}
		else if (txtPassword.getText().isEmpty()) {
			labelErrors.setText("You need to pass a password");
		}
	}

	@FXML
	private void onRegisterClick(ActionEvent event) {
		labelForInfo.setText("");
		labelErrors.setText("");


		try {
			String name = txtName.getText().trim();
			String surname = txtSurname.getText().trim();
			String login = txtLogin.getText().trim();
			String password = txtPassword.getText().trim();
			String phoneNumber = txtPhoneNumber.getText().trim();

			if (name.isEmpty() || surname.isEmpty() || login.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
				labelErrors.setText("Name, surname, login, password, and phone number cannot be empty");
				return;
			}

			Person person = AddPerson.request(name, surname);
			if (person == null) {
				labelErrors.setText("Failed to create the person. Probably name and surname are not unique.");
				return;
			}

			User user = AddUser.request(login, password, person);
			if (user == null) {
				DelPerson.request(person);
				labelErrors.setText("Failed to create user. Login might be already taken.");
				return;
			}

			Client client = AddClient.request(user, phoneNumber);
			if (client == null) {
				DelPerson.request(person);
				DelUser.request(user);
				labelErrors.setText("Failed to add client details. Remember that max length of the telephone number is 9 and must be unique.");
				return;
			}

			labelForInfo.setText("User added: " + login);
			labelErrors.setText("");
			CleraForm();

		} catch (Exception e) {
			labelErrors.setText("Some error occurred: " + e.getMessage());
		}
	}

	@FXML
	private void onClearClick(ActionEvent event) {
		CleraForm();
		labelForInfo.setText("");
		labelErrors.setText("");
	}

	@FXML
	private void onCancelClick(ActionEvent event) throws IOException {
		SceneController.switchScenes(event, "login", "css/login.css");
	}

	private void CleraForm() {
		txtLogin.clear();
		txtPassword.clear();
		txtName.clear();
		txtPhoneNumber.clear();
		txtSurname.clear();
	}
}
