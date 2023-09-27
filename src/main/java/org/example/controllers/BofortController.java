package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.GlobalVariables;
import org.example.lab2.BofortEncryptor;

public class BofortController {
	@FXML
	TextField keyText;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputEncryptedMessage;

	private BofortEncryptor encryptor;
	private BofortEncryptor.VARIANT variant = null;

	public void encrypt() {
		if (variant == null) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Нет выбранного варианта");
			errorPopup.setHeaderText("Ошибка: не был выбран вариант шифра");
			errorPopup.setContentText("Пожалуйста выберите один из двух предложенных формул шифра");
			errorPopup.showAndWait();
			return;
		}
		String key = this.keyText.getText();
		this.encryptor = new BofortEncryptor(key, this.variant);
		String message = this.inputMessage.getText();
		String encryptedText = this.encryptor.encrypt(message);
		this.outputEncryptedMessage.setText(encryptedText);
	}
	public void decrypt() {
		if (variant == null) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Нет выбранного варианта");
			errorPopup.setHeaderText("Ошибка: не был выбран вариант шифра");
			errorPopup.setContentText("Пожалуйста выберите один из двух предложенных формул шифра");
			errorPopup.showAndWait();
			return;
		}
		String key = this.keyText.getText();
		this.encryptor = new BofortEncryptor(key, this.variant);
		String message = this.inputMessage.getText();
		String decryptedText = this.encryptor.decrypt(message);
		this.outputEncryptedMessage.setText(decryptedText);
	}
	public void chooseVariant1(ActionEvent event) {
		this.variant = BofortEncryptor.VARIANT.ONE;
	}
	public void chooseVariant2(ActionEvent event) {
		this.variant = BofortEncryptor.VARIANT.TWO;
	}
}
