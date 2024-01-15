package org.togu.controllers.lab3;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.togu.common.*;
import org.togu.lab3.MatrixMultiplicationEncryptor;

public class MatrixMultiplicationController implements EncryptorForm, DecryptorForm {
	@FXML
	TextArea keyText; // Текстовая область заполнения ключа шифровки пользователем
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputEncryptedMessage; // Текстовая область вывода результата шифровки пользователю

	// По требованию лабораторной работы: используется кириллица
	public static final Alphabet USED_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	private final MatrixMultiplicationEncryptor encryptor = new MatrixMultiplicationEncryptor(USED_ALPHABET);
	private void setupEncryptor() throws IllegalStateException {
		String key = this.keyText.getText();
		try {
			this.encryptor.setKey(key);
		} catch (Exception e) {
			Alerts.showError(
				"Неправильный ключ",
				"Ошибка: неправильный ключ шифра",
				"Пожалуйста введите квадратную матрицу, определитель которой не равен 0.\nПолученная ошибка: " + e);
			throw new IllegalStateException("User entered invalid key");
		}
	}
	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		this.setupEncryptor();
		String message = this.inputMessage.getText();
		String encryptedMessage = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedMessage);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		this.setupEncryptor();
		String message = this.inputMessage.getText();
		String decryptedMessage = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedMessage);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputEncryptedMessage.getText());
		this.outputEncryptedMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Метод умножением матриц", "MatrixMultiplicationDescription.md");
	}
}
