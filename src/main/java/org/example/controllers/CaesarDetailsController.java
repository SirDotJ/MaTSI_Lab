package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.Alerts;
import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;

public class CaesarDetailsController {
	@FXML
	TextField keyValueInputField; // Текстовая область заполнения значения ключа шифровки пользователем
	@FXML
	TextArea alphabetOutput; // Поле вывода алфавитов для шифровки пользователю

	private static final Alphabet USED_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	private static final boolean DEFAULT_IN_LOWER_CASE = false;
	private static final String ALPHABET_SEPARATOR = "\n";

	private int getKey() throws NumberFormatException {
		return Integer.parseInt(this.keyValueInputField.getText());
	}

	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		int key;
		try {
			key = this.getKey();
		} catch (NumberFormatException exception) {
			Alerts.showError(
					"Неправильный ключ",
					"Ошибка: неправильный ключ шифра",
					"Пожалуйста введите только одну цифру в качестве ключа");
			return;
		}
		this.alphabetOutput.clear();
		this.alphabetOutput.appendText(USED_ALPHABET.toString(DEFAULT_IN_LOWER_CASE));
		this.alphabetOutput.appendText(ALPHABET_SEPARATOR);
		for (int i = 0; i < USED_ALPHABET.size(); i++) {
			int newLetterIndex = i + key;
			if (newLetterIndex > USED_ALPHABET.size() - 1)
				newLetterIndex -= USED_ALPHABET.size();
			char encryptedLetter = USED_ALPHABET.get(newLetterIndex, DEFAULT_IN_LOWER_CASE);
			this.alphabetOutput.appendText(String.valueOf(encryptedLetter));
		}
	}
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		int key;
		try {
			key = this.getKey();
		} catch (NumberFormatException exception) {
			Alerts.showError(
					"Неправильный ключ",
					"Ошибка: неправильный ключ шифра",
					"Пожалуйста введите только одну цифру в качестве ключа");
			return;
		}
		this.alphabetOutput.clear();
		this.alphabetOutput.appendText(USED_ALPHABET.toString(DEFAULT_IN_LOWER_CASE));
		this.alphabetOutput.appendText(ALPHABET_SEPARATOR);
		for (int i = 0; i < USED_ALPHABET.size(); i++) {
			int newLetterIndex = i - key;
			if (newLetterIndex < 0)
				newLetterIndex = USED_ALPHABET.size() - Math.abs(newLetterIndex);
			char decryptedLetter = USED_ALPHABET.get(newLetterIndex, DEFAULT_IN_LOWER_CASE);
			this.alphabetOutput.appendText(String.valueOf(decryptedLetter));
		}
	}
}
