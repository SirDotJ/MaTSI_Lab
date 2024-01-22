package org.togu.controllers.lab2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.togu.common.*;
import org.togu.controllers.HelpController;
import org.togu.lab2.BofortEncryptor;

public class BofortController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField keyText; // Поле заполнения ключа шифровки пользователем
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputEncryptedMessage; // Текстовая область вывода результата шифровки пользователю

	// По требованию лабораторной работы: используется кириллица
	public static final Alphabet USED_ALPHABET = AlphabetConstants.CYRILLIC_NO_SPACE;

	private final BofortEncryptor encryptor = new BofortEncryptor(USED_ALPHABET);
	private BofortEncryptor.VARIANT variant = null;

	public void chooseVariant1(ActionEvent event) { // Вызывается при выборе радиокнопки первой формы
		this.variant = BofortEncryptor.VARIANT.ONE;
		this.encryptor.setVariant(this.variant);
	}
	public void chooseVariant2(ActionEvent event) { // Вызывается при выборе радиокнопки второй формы
		this.variant = BofortEncryptor.VARIANT.TWO;
		this.encryptor.setVariant(this.variant);
	}
	private void setupEncryptor() throws IllegalStateException {
		if (variant == null) {
			Alerts.showError(
					"Нет выбранного варианта",
					"Ошибка: не был выбран вариант шифра",
					"Пожалуйста выберите один из двух предложенных формул шифра"
			);
			throw new IllegalStateException("Variant was not chosen by the user");
		}
		this.encryptor.setKey(this.keyText.getText());
	}

	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		try {
			this.setupEncryptor();
		} catch (IllegalStateException e) {
			return;
		}
		String message = this.inputMessage.getText();
		String encryptedText = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedText);
	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		try {
			this.setupEncryptor();
		} catch (IllegalStateException e) {
			return;
		}
		String message = this.inputMessage.getText();
		String decryptedText = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedText);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputEncryptedMessage.getText());
		this.outputEncryptedMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Шифр Бофорта", "BofortDescription.md");
	}
}
