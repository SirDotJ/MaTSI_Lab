package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.lab3.KnapsackEncryptor;
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
		this.encryptor = new MatrixMultiplicationEncryptor(key);
		String message = this.inputMessage.getText();
		String encryptedMessage = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedMessage);
	}
	public void decrypt() {
		String key = this.keyText.getText();
		this.encryptor = new MatrixMultiplicationEncryptor(key);
		String message = this.inputMessage.getText();
		String decryptedMessage = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedMessage);
	}
}
