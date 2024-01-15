package org.togu.controllers.lab1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.togu.common.Alerts;
import org.togu.common.DecryptorForm;
import org.togu.common.EncryptorForm;
import org.togu.common.HelpController;
import org.togu.lab1.HamiltonPathEncryptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HamiltonPathController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField foundHamiltonPathOutput; // Поле вывода текущего гамильтонового пути Пользователю
	@FXML
	TextArea messageInput; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea encryptedMessageOutput; // Текстовая область вывода результата шифровки пользователю

	// Матрица смежности определена в постановке лабораторной работы №1
	private final static ArrayList<ArrayList<Integer>> HAMILTON_ADJACENCY_MATRIX = new ArrayList<>(Arrays.asList(
			new ArrayList<>(Arrays.asList(0, 1, 0, 0, 0, 0, 1, 1)),
			new ArrayList<>(Arrays.asList(1, 0, 1, 1, 0, 0, 0, 0)),
			new ArrayList<>(Arrays.asList(0, 1, 0, 1, 1, 0, 0, 0)),
			new ArrayList<>(Arrays.asList(0, 1, 1, 0, 1, 1, 1, 1)),
			new ArrayList<>(Arrays.asList(0, 0, 1, 1, 0, 1, 0, 0)),
			new ArrayList<>(Arrays.asList(0, 0, 0, 1, 1, 0, 1, 0)),
			new ArrayList<>(Arrays.asList(1, 0, 0, 1, 0, 1, 0, 1)),
			new ArrayList<>(Arrays.asList(1, 0, 0, 1, 0, 0, 1, 0))
	));

	private final HamiltonPathEncryptor encryptor = new HamiltonPathEncryptor(HAMILTON_ADJACENCY_MATRIX);

	public void getNewPath() { // Вызывается при нажатии кнопки "Новый путь"
		ArrayList<Integer> newHamiltonPath = (ArrayList<Integer>) this.encryptor.generateHamiltonPath();
		try {
			this.setFoundHamiltonPathOutput(newHamiltonPath);
		} catch (IllegalArgumentException e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Ошибка получения пути");
			errorPopup.setHeaderText("Гамильтоновый путь не был построен");
			errorPopup.setContentText("Произошла ошибка при попытке создания нового гамильтонового пути: " + e);

			errorPopup.showAndWait();
		}
	}
	private void setFoundHamiltonPathOutput(ArrayList<Integer> path) throws IllegalArgumentException {
		if (path == null || path.isEmpty()) // проверка на создание набора
			throw new IllegalArgumentException("Переданный путь пустой");
		Set<Integer> presentValues = new HashSet<>(path); // проверка на корректность пути
		if (presentValues.size() != path.size())
			throw new IllegalArgumentException("Переданный путь не является гамильтоновым: вершины встречаются несколько раз");

		this.foundHamiltonPathOutput.clear();
		this.foundHamiltonPathOutput.appendText(String.valueOf((path.get(0))));
		for (int i = 1; i < path.size(); i++) {
			this.foundHamiltonPathOutput.appendText(" " + (path.get(i)));
		}
	}
	private ArrayList<Integer> getKey() {
		if (foundHamiltonPathOutput.getText().isEmpty())
			this.getNewPath();

		String keyText = foundHamiltonPathOutput.getText();
		ArrayList<String> splitKeyText = new ArrayList<>(Arrays.asList(keyText.split(" ")));
		ArrayList<Integer> hamiltonPath = new ArrayList<>();
		splitKeyText.forEach((substring) -> {
			int vertexNumber = Integer.parseInt(substring);
			hamiltonPath.add(vertexNumber);
		});

		return hamiltonPath;
	}
	public void openDetails() { // Вызывается при нажатии на кнопку "Детали" пользователем
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("MaTDP_HamiltonPathDetails.fxml"));

			Stage detailsStage = new Stage();
			Parent detailsRoot = loader.load();
			detailsStage.setTitle("Алгоритм Гамильтонового пути");
			detailsStage.setScene(new Scene(detailsRoot));

			HamiltonPathDetailsController controller = loader.getController();
			controller.initializeConnections();
			controller.setPath(this.foundHamiltonPathOutput.getText());

			detailsStage.show();
		} catch (Exception e) {
			Alerts.showError(
					"Неизвестная ошибка",
					"Произошла неизвестная ошибка",
					"Произошла ошибка при попытке открытия окна с деталями: " + e
			);
		}
	}

	@Override
	public void encrypt() { // Вызывается при нажатии кнопки "Зашифровать" пользователем
		ArrayList<Integer> key = this.getKey();
		this.encryptor.setHamiltonPath(key);

		String openMessage = this.messageInput.getText();
		String encryptedMessage = this.encryptor.encrypt(openMessage);
		this.encryptedMessageOutput.setText(encryptedMessage);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		ArrayList<Integer> key = this.getKey();
		this.encryptor.setHamiltonPath(key);

		String encryptedMessage = this.messageInput.getText();
		String decryptedMessage = this.encryptor.decrypt(encryptedMessage);
		this.encryptedMessageOutput.setText(decryptedMessage);
	}

	public void transferOutputToInput() {
		this.messageInput.setText(this.encryptedMessageOutput.getText());
		this.encryptedMessageOutput.setText("");
	}

	public void openHelp() {
		HelpController.open("Метод Гамильтонового пути", "HamiltonPathDescription.md");
	}
}
