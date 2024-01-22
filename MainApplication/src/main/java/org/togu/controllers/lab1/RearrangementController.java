package org.togu.controllers.lab1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.togu.common.Alerts;
import org.togu.common.DecryptorForm;
import org.togu.common.EncryptorForm;
import org.togu.controllers.HelpController;
import org.togu.lab1.RearrangementEncryptor;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class RearrangementController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField key1TextField; // Поле заполнения ключа шифровки №1 пользователем
	@FXML
	TextField key2TextField; // Поле заполнения ключа шифровки №2 пользователя
	@FXML
	TextArea messageTextArea; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea encryptedMessageTextArea; // Текстовая область вывода результата шифровки пользователю

	RearrangementEncryptor encryptor = new RearrangementEncryptor();

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

		ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split(" ")));
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

	private void setupEncryptor() throws IllegalStateException {
		ArrayList<Integer> key1;
		ArrayList<Integer> key2;
		try {
			key1 = this.getKey1();
			key2 = this.getKey2();
		} catch (Exception e) {
			Alerts.showError(
					"Неправильные ключи",
					"Ошибка: неправильный ключ шифра",
					"Пожалуйста введите набор чисел от 0 до n без повторений, где n - размер ключа"
			);
			throw new IllegalStateException("Invalid keys");
		}

		String encryptedMessage = this.messageTextArea.getText();
		if (encryptedMessage.length() > key1.size() * key2.size()) {
			Alerts.showError(
					"Неправильное сообщение",
					"Ошибка: неправильный размер сообщения для ключей",
					"Пожалуйста измените ключи для возможности шифровки или введите сообщение меньшего объема");
			throw new IllegalStateException("Invalid message");
		}
		this.encryptor.setKey1(key1);
		this.encryptor.setKey2(key2);
	}
	public void openDetails() { // Вызывается при нажатии на кнопку "Детали" пользователем
		try {
			FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("MaTDP_RearrangementDetails.fxml"));
			Parent detailsRoot = loader.load();
			RearrangementDetailsController controller = loader.getController();
			try {
				controller.displayEncryptionTable(this.encryptor.getEncryptionTable());
				controller.displayDecryptionTable(this.encryptor.getDecryptionTable());
			} catch (IllegalStateException e) {
				Alerts.showError(
						"Ошибка таблиц шифровки/расшифровки",
						"Нет установленной таблицы шифровки/расшифровки",
						"Пожалуйста введите используемые ключи и попробуйте снова");
				return;
			}

			Stage detailsStage = new Stage();
			detailsStage.setTitle("Алгоритм Перестановки");
			detailsStage.setScene(new Scene(detailsRoot));
			detailsStage.show();
		} catch (Exception e) {
			Alerts.showError(
					"Неизвестная ошибка",
					"Произошла неизвестная ошибка",
					"При попытке запуска окна с деталями произошла ошибка: " + e
			);
		}
	}

	@Override
	public void encrypt() { // Вызывается при нажатии на кнопку "Зашифровать" пользователем
		String openMessage = this.messageTextArea.getText();
		try {
			this.setupEncryptor();
		} catch (Exception e) {
			return;
		}

		String encryptedMessage = encryptor.encrypt(openMessage);
		this.encryptedMessageTextArea.setText(encryptedMessage);

	}
	@Override
	public void decrypt() { // Вызывается при нажатии на кнопку "Расшифровать" пользователем
		String encryptedMessage = this.messageTextArea.getText();
		try {
			this.setupEncryptor();
		} catch (Exception e) {
			return;
		}

		String decryptedMessage = encryptor.decrypt(encryptedMessage);
		this.encryptedMessageTextArea.setText(decryptedMessage);
	}

	public void transferOutputToInput() {
		this.messageTextArea.setText(this.encryptedMessageTextArea.getText());
		this.encryptedMessageTextArea.setText("");
	}

	public void openHelp() {
		HelpController.open("Шифрование перестановкой", "RearrangementDescription.md");
	}
}
