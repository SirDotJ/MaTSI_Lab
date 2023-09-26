package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.lab1.CaesarEncryptor;
import org.example.lab1.GlobalVariables;

import java.security.InvalidAlgorithmParameterException;

public class CaesarController {
	@FXML
	TextField keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;

	private CaesarEncryptor encryptor;
	private int getKey() throws NumberFormatException {
		return Integer.parseInt(keyText.getText());
	}
	private String getInput() throws InvalidAlgorithmParameterException {
		String currentText = this.inputMessage.getText();

		if (currentText.contains(" "))
			throw new InvalidAlgorithmParameterException("Text must not have spaces, use " + GlobalVariables.SPACE_CHARACTER + " instead");

		for (int i = 0; i < currentText.length(); i++) {
			char character = currentText.charAt(i);
			if (character == GlobalVariables.SPACE_CHARACTER)
				continue;
			if (!Character.UnicodeBlock.of(character).equals(Character.UnicodeBlock.CYRILLIC))
				throw new InvalidAlgorithmParameterException("Text must be all be Cyrillic or space characters");
		}

		return currentText;
	}
	public void encrypt() {
		int key;
		try {
			key = getKey();
		} catch (NumberFormatException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ключ");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите одно число в качестве ключа");
			errorPopup.showAndWait();
			return;
		}
		String rawText;
		try {
			rawText = this.getInput();
		} catch (InvalidAlgorithmParameterException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ввод");
			errorPopup.setHeaderText("Ошибка: неправильный формат введенного сообщения");
			errorPopup.setContentText("Пожалуйста введите сообщение из заглавных русских букв и с символом " + GlobalVariables.SPACE_CHARACTER + " вместо пробела");
			errorPopup.showAndWait();
			return;
		}

		this.encryptor = new CaesarEncryptor(key);
		String encryptedText = this.encryptor.encrypt(rawText);
		this.outputEncryptedMessage.setText(encryptedText);
	}
	public void decrypt() {
		int key;
		try {
			key = getKey();
		} catch (NumberFormatException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ключ");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите только одну цифру в качестве ключа");
			errorPopup.showAndWait();
			return;
		}
		String encryptedText;
		try {
			encryptedText = this.getInput();
		} catch (InvalidAlgorithmParameterException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ввод");
			errorPopup.setHeaderText("Ошибка: неправильный формат введенного сообщения");
			errorPopup.setContentText("Пожалуйста введите сообщение из заглавных русских букв и с символом " + GlobalVariables.SPACE_CHARACTER + " вместо пробела");
			errorPopup.showAndWait();
			return;
		}

		this.encryptor = new CaesarEncryptor(key);
		String decryptedText = this.encryptor.decrypt(encryptedText);
		this.outputEncryptedMessage.setText(decryptedText);
	}
	public void openDetails() {
		try {
			Parent detailsRoot = FXMLLoader.load(this.getClass().getClassLoader().getResource("MaTDP_CaesarDetails.fxml"));
			Stage detailsStage = new Stage();
			detailsStage.setTitle("Алгоритм Цезаря");
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
