package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.*;
import org.example.lab4.Playfair;


public class PlayfairController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;

	public static final Alphabet USED_CYRILLIC_ALPHABET = AlphabetConstants.CYRILLIC_NO_SPACE;
	public static final Alphabet USED_LATIN_ALPHABET = AlphabetConstants.LATIN_NO_SPACE;
	private final Playfair encryptor = new Playfair();
	private boolean initializeMatrix() {
		if (keyText.getText().isEmpty()) {
			Alerts.showError(
					"Недопустимый ключ",
					"Ключ пустой",
					"Пожалуйста введите ключ перед использованием шифра"
			);
			return false;
		}
		this.encryptor.setKey(this.keyText.getText(), getSelectedAlphabet());
		return true;
	}
	private Alphabet getSelectedAlphabet() {
		if (cyrillicRadioButton.isSelected())
			return USED_CYRILLIC_ALPHABET;
		else
			return USED_LATIN_ALPHABET;
	}
	@Override
	public void encrypt() {
		if (!initializeMatrix())
			return;
		String encryptedText = this.encryptor.encrypt(this.inputMessage.getText());
		this.outputEncryptedMessage.setText(encryptedText);
	}

	@Override
	public void decrypt() {
		if (!initializeMatrix())
			return;
		String decryptedText = this.encryptor.decrypt(this.inputMessage.getText());
		this.outputEncryptedMessage.setText(decryptedText);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputEncryptedMessage.getText());
		this.outputEncryptedMessage.setText("");
	}

	public void openHelp() {
		return;
	}
}
