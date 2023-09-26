package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.lab2.VigenereEncryptor;

public class VigenereController {
	@FXML
	TextField keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;

	private VigenereEncryptor encryptor;
	public void encrypt() {
		String key = keyText.getText();
		this.encryptor = new VigenereEncryptor(key);
		String encryptedText = this.encryptor.encrypt(this.inputMessage.getText());
		this.outputEncryptedMessage.setText(encryptedText);
	}
	public void decrypt() {
		String key = keyText.getText();
		this.encryptor = new VigenereEncryptor(key);
		String decryptedText = this.encryptor.decrypt(this.inputMessage.getText());
		this.outputEncryptedMessage.setText(decryptedText);
	}
}


