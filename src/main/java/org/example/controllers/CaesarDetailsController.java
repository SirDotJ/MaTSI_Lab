package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.lab1.CaesarEncryptor;

import org.example.common.GlobalVariables;

public class CaesarDetailsController {
	@FXML
	TextArea alphabetOutput;
	@FXML
	TextField keyValueInputField;
	private int getKey() throws NumberFormatException {
		return Integer.parseInt(this.keyValueInputField.getText());
	}
	public void encrypt() {
		int key;
		try {
			key = this.getKey();
		} catch (NumberFormatException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ключ");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите только одну цифру в качестве ключа");
			errorPopup.showAndWait();
			return;
		}
		this.alphabetOutput.clear();
		for (int i = 0; i < GlobalVariables.CYRILLIC_ALPHABET_SIZE; i++) {
			this.alphabetOutput.appendText(String.valueOf(CaesarEncryptor.CYRILLIC_ALPHABET_UPPERCASE.get(i)));
		}
		this.alphabetOutput.appendText("\n");
		for (int i = 0; i < GlobalVariables.CYRILLIC_ALPHABET_SIZE; i++) {
			int newLetterIndex = i + key;
			if (newLetterIndex > GlobalVariables.CYRILLIC_ALPHABET_SIZE - 1)
				newLetterIndex -= GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			this.alphabetOutput.appendText(String.valueOf(CaesarEncryptor.CYRILLIC_ALPHABET_UPPERCASE.get(newLetterIndex)));
		}
	}
	public void decrypt() {
		int key;
		try {
			key = this.getKey();
		} catch (NumberFormatException exception) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильный ключ");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите только одну цифру в качестве ключа");
			errorPopup.showAndWait();
			return;
		}
		this.alphabetOutput.clear();
		for (int i = 0; i < GlobalVariables.CYRILLIC_ALPHABET_SIZE; i++) {
			this.alphabetOutput.appendText(String.valueOf(CaesarEncryptor.CYRILLIC_ALPHABET_UPPERCASE.get(i)));
		}
		this.alphabetOutput.appendText("\n");
		for (int i = 0; i < GlobalVariables.CYRILLIC_ALPHABET_SIZE; i++) {
			int newLetterIndex = i - key;
			if (newLetterIndex < 0)
				newLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_SIZE - Math.abs(newLetterIndex);
			this.alphabetOutput.appendText(String.valueOf(CaesarEncryptor.CYRILLIC_ALPHABET_UPPERCASE.get(newLetterIndex)));
		}
	}
}
