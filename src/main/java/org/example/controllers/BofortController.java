package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.lab2.BofortEncryptor;

public class BofortController {
	@FXML
	TextField keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;

	private BofortEncryptor encryptor;

	public void encrypt() {
		String key = this.keyText.getText();
		this.encryptor = new BofortEncryptor(key);
		String message = this.inputMessage.getText();
		String encryptedText = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedText);
	}
	public void decrypt() {
		String key = this.keyText.getText();
		this.encryptor = new BofortEncryptor(key);
		String message = this.inputMessage.getText();
		String decryptedText = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedText);
	}
}
