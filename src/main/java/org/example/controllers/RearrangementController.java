package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.example.lab1.RearrangementEncryptor;

import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class RearrangementController {
	@FXML
	TextField key1TextField;
	@FXML
	TextField key2TextField;
	@FXML
	TextArea messageTextArea;
	@FXML
	TextArea encryptedMessageTextArea;

	RearrangementEncryptor encryptor;

	private ArrayList<Integer> getKey1() throws InvalidAlgorithmParameterException {
		ArrayList<Integer> output;
		String data = key1TextField.getText();
		try {
			output = parseData(data);
		} catch (NumberFormatException e) {
			throw new InvalidAlgorithmParameterException("Invalid key1 data");
		}
		return output;
	}
	private ArrayList<Integer> getKey2() throws InvalidAlgorithmParameterException {
		ArrayList<Integer> output;
		String data = key2TextField.getText();
		try {
			output = parseData(data);
		} catch (Exception e) {
			throw new InvalidAlgorithmParameterException("Invalid key2 data");
		}
		return output;
	}
	private ArrayList<Integer> parseData(String data) throws NumberFormatException, InvalidAlgorithmParameterException {
		ArrayList<Integer> output = new ArrayList<>();

		data = data.replace("[", "");
		data = data.replace("]", "");

		ArrayList<String> dataList = new ArrayList<String>(Arrays.asList(data.split(" ")));
		for (String input :
				dataList) {
			int number = Integer.parseInt(input);
			if (output.contains(number))
				throw new InvalidAlgorithmParameterException();
			else
				output.add(number);
		}

		return output;
	}

	public void encrypt() {
		ArrayList<Integer> key1;
		ArrayList<Integer> key2;
		try {
			key1 = this.getKey1();
			key2 = this.getKey2();
		} catch (Exception e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильные ключи");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите только одну цифру в качестве ключа");
			errorPopup.showAndWait();
			return;
		}

		String message = this.messageTextArea.getText();
		if (message.length() > key1.size() * key2.size()) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильное сообщение");
			errorPopup.setHeaderText("Ошибка: неправильный размер сообщения для ключей");
			errorPopup.setContentText("Пожалуйста измените ключи для возможности шифровки или введите сообщение меньшего объема");
			errorPopup.showAndWait();
			return;
		}

		this.encryptor = new RearrangementEncryptor(key1, key2);
		String encryptedMessage = encryptor.encrypt(message);
		this.encryptedMessageTextArea.setText(encryptedMessage);
	}
	public void decrypt() {
		ArrayList<Integer> key1;
		ArrayList<Integer> key2;
		try {
			key1 = this.getKey1();
			key2 = this.getKey2();
		} catch (Exception e) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильные ключи");
			errorPopup.setHeaderText("Ошибка: неправильный ключ шифра");
			errorPopup.setContentText("Пожалуйста введите только одну цифру в качестве ключа");
			errorPopup.showAndWait();
			return;
		}

		String encryptedMessage = this.messageTextArea.getText();
		if (encryptedMessage.length() > key1.size() * key2.size()) {
			Alert errorPopup = new Alert(Alert.AlertType.ERROR);
			errorPopup.setTitle("Неправильное сообщение");
			errorPopup.setHeaderText("Ошибка: неправильный размер сообщения для ключей");
			errorPopup.setContentText("Пожалуйста измените ключи для возможности шифровки или введите сообщение меньшего объема");
			errorPopup.showAndWait();
			return;
		}

		this.encryptor = new RearrangementEncryptor(key1, key2);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);
		this.encryptedMessageTextArea.setText(decryptedMessage);
	}
}
