package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.common.*;
import org.example.lab1.CaesarEncryptor;

import java.security.InvalidAlgorithmParameterException;

public class CaesarController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField keyText; // Поле заполнения ключа шифровки пользователем
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputEncryptedMessage; // Текстовая область вывода результата шифровки пользователю

	// По требованию лабораторной работы: пробелы игнорируются, работаем с кириллицей
	public static final Alphabet USED_ALPHABET = AlphabetConstants.CYRILLIC_NO_SPACE;

	private final CaesarEncryptor encryptor = new CaesarEncryptor(USED_ALPHABET);

	private int getKey() throws InvalidAlgorithmParameterException {
		try {
			return Integer.parseInt(this.keyText.getText());
		} catch (NumberFormatException e) {
			Alerts.showError(
					"Неправильный ключ",
					"Ошибка: неправильный ключ шифра",
					"Пожалуйста введите одно целое число в качестве ключа"
			);
			throw new InvalidAlgorithmParameterException("Key could not be parsed to number");
		}
	}
	private String getInput() throws InvalidAlgorithmParameterException {
		String currentText = this.inputMessage.getText();

		// Проверка на пробелы
		if (currentText.contains(" ")) {
			Alerts.showError(
				"Неправильное сообщение",
				"Был использован символ пробела",
				"Пожалуйста вместо пробела используйте символ " + AlphabetConstants.SPACE_CHARACTER
			);
			throw new InvalidAlgorithmParameterException("Invalid space character used");
		}

		return currentText;
	}
	public void openDetails() { // Вызывается при нажатии на кнопку "Детали" пользователем
		try {
			Parent detailsRoot = FXMLLoader.load(this.getClass().getClassLoader().getResource("MaTDP_CaesarDetails.fxml"));
			Stage detailsStage = new Stage();
			detailsStage.setTitle("Алгоритм Цезаря");
			detailsStage.setScene(new Scene(detailsRoot));
			detailsStage.show();
		} catch (Exception e) {
			Alerts.showError(
					"Ошибка открытия деталей",
					"Произошла ошибка при открытии деталей",
					"Ошибка при открытии формы: " + e
			);
		}
	}

	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		try {
			this.encryptor.setKey(this.getKey());
		} catch (InvalidAlgorithmParameterException e) {
			return;
		}

		String openMessage;
		try {
			openMessage = this.getInput();
		} catch (InvalidAlgorithmParameterException exception) {
			return;
		}

		String encryptedText = this.encryptor.encrypt(openMessage);
		this.outputEncryptedMessage.setText(encryptedText);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		try {
			this.encryptor.setKey(this.getKey());
		} catch (InvalidAlgorithmParameterException e) {
			return;
		}

		String encryptedText;
		try {
			encryptedText = this.getInput();
		} catch (InvalidAlgorithmParameterException exception) {
			return;
		}

		String decryptedText = this.encryptor.decrypt(encryptedText);
		this.outputEncryptedMessage.setText(decryptedText);
	}
}
