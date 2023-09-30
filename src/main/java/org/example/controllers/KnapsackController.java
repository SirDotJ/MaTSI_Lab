package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.lab3.KnapsackEncryptor;

public class KnapsackController {
	@FXML
	TextField keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;
	private KnapsackEncryptor encryptor;
	public void encrypt() {
		String key = this.keyText.getText();
		if (key.split(" ").length != KnapsackEncryptor.BINARY_REPRESENTATION_LENGTH) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Ошибка ключа");
			errorPopup.setHeaderText("Неправильный ключ");
			errorPopup.setContentText("Пожалуйста введите ключ из " + KnapsackEncryptor.BINARY_REPRESENTATION_LENGTH + " чисел");
			errorPopup.showAndWait();
			return;
		}
		this.encryptor = new KnapsackEncryptor(key);
		String message = this.inputMessage.getText();
		String encryptedMessage = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedMessage);
	}
	public void decrypt() {
		String key = this.keyText.getText();
		this.encryptor = new KnapsackEncryptor(key);
		String message = this.inputMessage.getText();
		String decryptedMessage = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedMessage);
	}
}
