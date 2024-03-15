package org.openjfx.controller;

import java.io.IOException;

import org.openjfx.requests.AddUser;
import org.openjfx.requests.GetUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class RegisterController {
	@FXML
	private Label labelForInfo;
	@FXML
	private Label labelErrors;
	@FXML
	private TextField txtLogin;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnRegister;

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
		this.signaliseIfNoInput();

		var user = GetUser.request(txtLogin.getText());
		if (user != null) {
			labelForInfo.setText("");
			labelErrors.setText("Login is already taken");
		}
		else {
			user = AddUser.request(txtLogin.getText(), txtPassword.getText());
			if (user != null) {
				labelErrors.setText("");
				labelForInfo.setText("Użytkownik został dodany: " + user.getLogin());
			}
		}
	}

	@FXML
	private void onCancelClick(ActionEvent event) throws IOException {
		SceneController.switchScenes(event, "login", "css/login.css");
	}
}
