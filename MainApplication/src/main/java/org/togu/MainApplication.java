package org.togu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

	private static final String APPLICATION_TITLE = "Тренажёр информационной безопасности";
	private static final String SCENE_FILE = "/MaTDP_Foundation.fxml";
	public static void main(String[] args) {
		LOGGER.info("Launching application...");
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		try {
			setupApplication(primaryStage);
		} catch (IOException ioException) {
			System.out.println("Ошибка: файл " + SCENE_FILE + " не был найден, проверьте что он находится в разделе \"\\resources\"");
			return;
		}
		primaryStage.show();
		LOGGER.info("Application's stage is shown and started successfully!");
	}
	private void setupApplication(Stage stage) throws IOException {
		Scene applicationScene = setupScene();
		stage.setScene(applicationScene);
		stage.setTitle(APPLICATION_TITLE);
		LOGGER.info("Application is set up!");
	}
	private Scene setupScene() throws IOException {
		Pane root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(SCENE_FILE)));
		return new Scene(root);
	}
}
