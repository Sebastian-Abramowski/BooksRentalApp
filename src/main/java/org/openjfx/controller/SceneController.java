package org.openjfx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import org.openjfx.App;
import org.openjfx.database.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {

	private static User currentUser = null;

	public static Scene getSceneFromFxml(String fxmlFileName) throws IOException {
		return new Scene(getParentFromFxml(fxmlFileName));
	}

	public static Parent getParentFromFxml(String fxmlFileName) throws IOException {
		String path = "src/main/resources/org/openjfx/" + fxmlFileName + ".fxml";
		FXMLLoader fxmlLoader = new FXMLLoader();
		FileInputStream fxmlFileStream = new FileInputStream(new File(path));
		return fxmlLoader.load(fxmlFileStream);
	}

	public static void switchScenes(ActionEvent event, String fxmlFileName) throws IOException {
//		System.out.println("\n\nSwitching scene to "+ fxmlFileName);
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Scene scene = SceneController.getSceneFromFxml(fxmlFileName);
		stage.setScene(scene);
		stage.show();
	}

	public static void switchScenes(ActionEvent event, String fxmlFileName, String cssPath) throws IOException {
//		System.out.println("\n\nSwitching scene to "+ fxmlFileName);
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Scene scene = SceneController.getSceneFromFxml(fxmlFileName);
		scene.getStylesheets().add(Objects.requireNonNull(App.class.getResource(cssPath)).toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	public static void signIn(ActionEvent event, User user) {
		try {
			System.out.println(user.getLogin());
			currentUser = user;
			SceneController.switchScenes(event, "UserViewMain", "css/buttons.css");
		}
		catch (IOException e) {
			currentUser = null;
			System.out.println("\n\nCould not signIn\nerror:\n" + e);
		}
	}

	public static void signOut(ActionEvent event) {
		currentUser = null;
		try {
			SceneController.switchScenes(event, "login", "css/login.css");
			currentUser = null;
		}
		catch (IOException e) {
			System.out.println("\n\nCould not signOut\nerror:\n" + e);
		}
	}

	public static User getCurrentUser() { return currentUser; }
}
