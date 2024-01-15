package org.togu.controllers.lab3;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.togu.common.*;
import org.togu.lab3.KnapsackEncryptor;

public class KnapsackController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField keyText; // Поле заполнения ключа шифровки пользователем
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputEncryptedMessage; // Текстовая область вывода результата шифровки пользователю

	// По требованию лабораторной работы: используется кириллица
	public static final Alphabet USED_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;

	private final KnapsackEncryptor encryptor = new KnapsackEncryptor(USED_ALPHABET);
	private void setupEncryptor() throws IllegalStateException {
		String key = this.keyText.getText();
		if (key.split(" ").length != KnapsackEncryptor.BINARY_REPRESENTATION_LENGTH) {
			Alerts.showError(
					"Ошибка ключа",
					"Неправильный ключ",
					"Пожалуйста введите ключ из " + KnapsackEncryptor.BINARY_REPRESENTATION_LENGTH + " чисел"
			);
			throw new IllegalStateException("User entered invalid key");
		}

		this.encryptor.setKey(key);
	}

	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		try {
			this.setupEncryptor();
		} catch (IllegalStateException e) {
			return;
		}

		String message = this.inputMessage.getText();
		String encryptedMessage = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedMessage);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		try {
			this.setupEncryptor();
		} catch (IllegalStateException e) {
			return;
		}

		String message = this.inputMessage.getText();
		String decryptedMessage = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedMessage);
	}

	public void openHelp() {
		HelpController.open("Шифр укладки ранца", "KnapsackDescription.md");
	}
}
