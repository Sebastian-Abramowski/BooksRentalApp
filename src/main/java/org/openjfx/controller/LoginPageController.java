package org.openjfx.controller;

import java.io.IOException;

import org.openjfx.requests.GetUser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class LoginPageController {
	@FXML
	private Label labelErrors;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private TextField txtLogin;
	@FXML
	private Button btnSignIn;
	@FXML
	private Button btnSignUp;
	@FXML
	private ToggleGroup userType;

	private void signaliseIfNoInput() {
		if (txtLogin.getText().isEmpty()) {
			labelErrors.setText("No login");
		}
		else if (txtPassword.getText().isEmpty()) {
			labelErrors.setText("No password");
		}
	}

	@FXML
	private void onSingInClick(ActionEvent event) {
		this.signaliseIfNoInput();

		var user = GetUser.request(txtLogin.getText(), txtPassword.getText());
		if (user == null) {
			labelErrors.setText("Incorrect login or password");
			return;
		}

		SceneController.signIn(event, user);
	}

	@FXML
	private void onSignUpClick(ActionEvent event) throws IOException {
		SceneController.switchScenes(event, "register", "css/buttons.css");
	}
}
