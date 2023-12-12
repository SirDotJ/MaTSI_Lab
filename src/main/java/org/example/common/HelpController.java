package org.example.common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HelpController {
	private static final String FORM_NAME = "/MaTDP_Method_Help.fxml";

	@FXML
	Label title;
	@FXML
	TextArea description;

	public static void open(String title, String fileName) {

		String descriptionContent;
		try {
			descriptionContent = Files.readString(Path.of(HelpController.class.getClassLoader().getResource("help/" + fileName).toURI()));
		} catch (java.net.URISyntaxException | IOException e) {
			Alerts.showError(
					"No file found",
					"Couldn't find file in resources",
					"Please check that file " + fileName + " exists in help folder"
			);
			return;
		}

		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(HelpController.class.getResource(FORM_NAME));
		HelpController controller = new HelpController();
		loader.setController(controller);

		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			Alerts.showError(
				"Loading error",
				"Exception occurred while trying to load help form",
				e.toString()
			);
			return;
		}

		controller.title.setText(title);
		controller.description.setText(descriptionContent);

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
