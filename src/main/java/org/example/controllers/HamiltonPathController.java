package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.lab1.HamiltonPathEncryptor;

import java.util.ArrayList;
import java.util.Arrays;

public class HamiltonPathController {
	public final static ArrayList<ArrayList<Integer>> HAMILTON_ADJACENCY_MATRIX = new ArrayList<>(Arrays.asList(
			new ArrayList<>(Arrays.asList(0, 1, 0, 0, 0, 0, 1, 1)),
			new ArrayList<>(Arrays.asList(1, 0, 1, 1, 0, 0, 0, 0)),
			new ArrayList<>(Arrays.asList(0, 1, 0, 1, 1, 0, 0, 0)),
			new ArrayList<>(Arrays.asList(0, 1, 1, 0, 1, 1, 1, 1)),
			new ArrayList<>(Arrays.asList(0, 0, 1, 1, 0, 1, 0, 0)),
			new ArrayList<>(Arrays.asList(0, 0, 0, 1, 1, 0, 1, 0)),
			new ArrayList<>(Arrays.asList(1, 0, 0, 1, 0, 1, 0, 1)),
			new ArrayList<>(Arrays.asList(1, 0, 0, 1, 0, 0, 1, 0))
	));

	@FXML
	TextField foundHamiltonPathOutput;
	@FXML
	TextArea messageInput;
	@FXML
	TextArea encryptedMessageOutput;

	private HamiltonPathEncryptor encryptor = new HamiltonPathEncryptor(HAMILTON_ADJACENCY_MATRIX);
	private void setFoundHamiltonPathOutput(ArrayList<Integer> path) {
		if (path == null || path.isEmpty())
			return;
		this.foundHamiltonPathOutput.clear();
		this.foundHamiltonPathOutput.appendText(String.valueOf((path.get(0))));
		for (int i = 1; i < path.size(); i++) {
			this.foundHamiltonPathOutput.appendText(" " + (path.get(i)));
		}
	}
	ArrayList<Integer> getKey() throws IllegalArgumentException {
		if (foundHamiltonPathOutput.getText().isEmpty())
			this.getNewPath();

		ArrayList<Integer> output = new ArrayList<>();
		String data = foundHamiltonPathOutput.getText();
		ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split(" ")));

		dataList.forEach((input) -> {
			int vertexNumber = Integer.parseInt(input);
			if (output.contains(vertexNumber))
				throw new IllegalArgumentException("Path must not repeat any vertex");
			output.add(vertexNumber);
		});
		if (output.size() != 8) // 8 - из выбранного графа
			throw new IllegalArgumentException("Must be path through all vertices");

		return output;
	}
	public void getNewPath() {
		ArrayList<Integer> newHamiltonPath = this.encryptor.generateHamiltonPath();
		this.encryptor.setHamiltonPath(newHamiltonPath);
		this.setFoundHamiltonPathOutput(newHamiltonPath);
	}
	public void encrypt() {
		ArrayList<Integer> key;
		try {
			key = getKey();
		} catch (Exception e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Ошибка чтения ключа");
			errorPopup.setHeaderText("Произошла ошибка при чтении ключа");
			errorPopup.setContentText("Пожалуйста проверьте, что путь проходит через все вершины один раз и номера вершин разделены пробелом");
			errorPopup.showAndWait();
			return;
		}
		this.encryptor.setHamiltonPath(key);
		String message = this.messageInput.getText();
		String encryptedMessage = this.encryptor.encrypt(message);
		this.encryptedMessageOutput.setText(encryptedMessage);
	}
	public void decrypt() {
		ArrayList<Integer> key;
		try {
			key = getKey();
		} catch (Exception e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Ошибка чтения ключа");
			errorPopup.setHeaderText("Произошла ошибка при чтении ключа");
			errorPopup.setContentText("Пожалуйста проверьте, что путь проходит через все вершины один раз и номера вершин разделены пробелом");
			errorPopup.showAndWait();
			return;
		}
		this.encryptor.setHamiltonPath(key);
		String encryptedMessage = this.messageInput.getText();
		String decryptedMessage = this.encryptor.decrypt(encryptedMessage);
		this.encryptedMessageOutput.setText(decryptedMessage);
	}
	public void openDetails() {
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("MaTDP_HamiltonPathDetails.fxml"));
			Parent detailsRoot = loader.load();
//			RearrangementDetailsController controller = loader.getController();
//			controller.displayEncryptionTable(this.encryptor.getEncryptionTable());

			Stage detailsStage = new Stage();
			detailsStage.setTitle("Алгоритм Гамильтонового пути");
			detailsStage.setScene(new Scene(detailsRoot));
			detailsStage.show();
		} catch (Exception e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неизвестная ошибка");
			errorPopup.setHeaderText("Произошла неизвестная ошибка");
			errorPopup.setContentText("Окно с деталями не смогло запуститься");
			e.printStackTrace();
			errorPopup.showAndWait();
		}
	}
}
