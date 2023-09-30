package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.example.lab3.MatrixMultiplicationEncryptor;

public class MatrixMultiplicationController {
	@FXML
	TextArea keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;
	private MatrixMultiplicationEncryptor encryptor;
	public void encrypt() {
		String key = this.keyText.getText();
		try {
			this.encryptor = new MatrixMultiplicationEncryptor(key);
			String message = this.inputMessage.getText();
			String encryptedMessage = this.encryptor.encrypt(message);
			this.outputEncryptedMessage.setText(encryptedMessage);
		} catch (Exception e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ключ");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите квадратную матрицу, определитель которой не равен 0");
			errorPopup.showAndWait();
		}
	}
	public void decrypt() {
		String key = this.keyText.getText();
		try {
			this.encryptor = new MatrixMultiplicationEncryptor(key);
			String message = this.inputMessage.getText();
			String decryptedMessage = this.encryptor.decrypt(message);
			this.outputEncryptedMessage.setText(decryptedMessage);
		} catch (IllegalArgumentException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ключ");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите квадратную матрицу, определитель которой не равен 0");
			errorPopup.showAndWait();
		}
	}
}
