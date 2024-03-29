package org.togu.controllers.lab2;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.togu.common.*;
import org.togu.controllers.HelpController;
import org.togu.lab2.VigenereEncryptor;

public class VigenereController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField keyText; // Поле заполнения ключа шифровки пользователем
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputEncryptedMessage; // Текстовая область вывода результата шифровки пользователю

	// По требованию лабораторной работы: используется кириллица
	public static final Alphabet USED_ALPHABET = AlphabetConstants.CYRILLIC_NO_SPACE;
	private final VigenereEncryptor encryptor = new VigenereEncryptor(USED_ALPHABET);

	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		this.encryptor.setKey(this.keyText.getText());
		String encryptedText = this.encryptor.encrypt(this.inputMessage.getText());
		this.outputEncryptedMessage.setText(encryptedText);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		this.encryptor.setKey(this.keyText.getText());
		String decryptedText = this.encryptor.decrypt(this.inputMessage.getText());
		this.outputEncryptedMessage.setText(decryptedText);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputEncryptedMessage.getText());
		this.outputEncryptedMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Шифр Виженера", "VigenereDescription.md");
	}
}


