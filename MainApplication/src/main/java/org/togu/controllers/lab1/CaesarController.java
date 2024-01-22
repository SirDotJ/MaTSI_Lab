package org.togu.controllers.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.togu.common.*;
import org.togu.controllers.HelpController;
import org.togu.lab1.CaesarEncryptor;

import java.security.InvalidAlgorithmParameterException;

public class CaesarController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField keyText; // Поле заполнения ключа шифровки пользователем
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputEncryptedMessage; // Текстовая область вывода результата шифровки пользователю

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;

	public static final Alphabet USED_CYRILLIC_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	public static final Alphabet USED_LATIN_ALPHABET = AlphabetConstants.LATIN_WITH_SPACE;

	private final CaesarEncryptor encryptor = new CaesarEncryptor();

	private Alphabet getSelectedAlphabet() {
		if (cyrillicRadioButton.isSelected())
			return USED_CYRILLIC_ALPHABET;
		else
			return USED_LATIN_ALPHABET;
	}

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
	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		this.encryptor.setNewAlphabet(this.getSelectedAlphabet());
		try {
			this.encryptor.setKey(this.getKey());
		} catch (InvalidAlgorithmParameterException e) {
			return;
		}

		String openMessage = this.inputMessage.getText();
		String encryptedText = this.encryptor.encrypt(openMessage);
		this.outputEncryptedMessage.setText(encryptedText);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		this.encryptor.setNewAlphabet(this.getSelectedAlphabet());
		try {
			this.encryptor.setKey(this.getKey());
		} catch (InvalidAlgorithmParameterException e) {
			return;
		}

		String encryptedText = this.inputMessage.getText();
		String decryptedText = this.encryptor.decrypt(encryptedText);
		this.outputEncryptedMessage.setText(decryptedText);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputEncryptedMessage.getText());
		this.outputEncryptedMessage.setText("");
	}

	public void increaseKey() {
		int value = Integer.parseInt(this.keyText.getText());
		value++;
		this.keyText.setText(String.valueOf(value));
	}

	public void decreaseKey() {
		int value = Integer.parseInt(this.keyText.getText());
		value--;
		if (value < 0)
			return;
		this.keyText.setText(String.valueOf(value));
	}

	public void parseKey(KeyEvent event) {
		int previousCaret = this.keyText.getCaretPosition();
		String previous = this.keyText.getText();
		String current = previous.replaceAll("\\D+", ""); // Get rid of non numbers
		this.keyText.setText(current);
		boolean isNumber = false;
		try {
			int number = Integer.parseInt(event.getCharacter());
			isNumber = true;
		} catch (Exception ignore) {}

		this.keyText.positionCaret(previousCaret + (isNumber ? 0 : -1));
	}

	public void openHelp() {
		HelpController.open("Алгоритм Цезаря", "CaesarDescription.md");
	}
}
