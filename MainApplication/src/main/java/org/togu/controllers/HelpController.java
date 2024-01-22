package org.togu.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.togu.common.Alerts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class HelpController {
	private static final String FORM_NAME = "/MaTDP_Method_Help.fxml";

	@FXML
	Label title;
	@FXML
	TextArea description;

	public static void open(String title, String fileName) {

		String descriptionContent;
		InputStream helpStream = HelpController.class.getClassLoader().getResourceAsStream("help/" + fileName);
		if (helpStream == null) {
			Alerts.showError(
				"Файл не найден в ресурсах",
				String.format("Файл под именем \"%s\" не был найден в ресурсах", fileName),
				"Сообщите разработчику программного средства о данной ошибке (пункт меню \"Помощь\"->\"Связь с разработчиком\")"
			);
			return;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(helpStream));
		descriptionContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));

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
