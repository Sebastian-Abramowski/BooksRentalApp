package org.openjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.controller.SceneController;
import org.openjfx.database.Database;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		Scene scene = SceneController.getSceneFromFxml("login");
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/login.css")).toExternalForm());
		stage.setTitle("Bookshop App");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);

		System.out.println("closing database...");
		Database db = Database.getInstance();
		db.close();
	}
}
