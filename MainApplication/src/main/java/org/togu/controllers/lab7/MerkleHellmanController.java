package org.togu.controllers.lab7;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.togu.common.*;
import org.togu.controllers.HelpController;
import org.togu.lab7.MerkleHellman;

import java.util.ArrayList;
import java.util.List;

public class MerkleHellmanController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField bitLengthInputTextField;
	@FXML
	TextField superincreasingListInputTextField;

	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputMessage;

	private final MerkleHellman encryptor = new MerkleHellman(AlphabetConstants.FULL_KEYBOARD);
	private int getBitLength() {
		return Integer.parseInt(this.bitLengthInputTextField.getText());
	}
	private List<Integer> getSuperincreasingList() {
		List<Integer> superIncreasingList = new ArrayList<>();
		String[] numbers = this.superincreasingListInputTextField.getText().split(" ");
		for (String number : numbers) {
			superIncreasingList.add(Integer.parseInt(number));
		}
		return superIncreasingList;
	}

	private boolean initializeCryptosystem() {
		int bitLength = this.getBitLength();
		List<Integer> superincreasingList = this.getSuperincreasingList();
		try {
			this.encryptor.setNewKey(bitLength, superincreasingList);
		} catch (IllegalArgumentException e) {
			Alerts.showError(
					"Недопустимые параметры криптосистемы",
					"Параметры криптосистемы недопустимы для Меркла-Хеллмана",
					"Пожалуйста введите положительное количество бит и сверхвозрастающую последовательность чисел (разделенных пробелом) того же размера"
			);
			return false;
		}
		return true;
	}

	public void generateSuperincreasingList() {
		int bitLength = this.getBitLength();

		if (bitLength < 0) {
			Alerts.showError(
					"Недопустимое количество бит",
					"Количество бит отрицательное",
					"Пожалуйста введите целое положительное количество бит"
			);
			return;
		}

		int changeCeiling = 20;
		int runningSum = (int) (Math.random() * changeCeiling);
		this.superincreasingListInputTextField.setText(String.valueOf(runningSum));

		for (int i = 1; i < bitLength; i++) {
			int generatedValue = (int) (Math.random() * changeCeiling + runningSum + 1);
			runningSum += generatedValue;
			this.superincreasingListInputTextField.appendText(" " + generatedValue);
		}
	}

	@Override
	public void encrypt() {
		if (!this.initializeCryptosystem())
			return;
		String encryptedText = this.encryptor.encrypt(this.inputMessage.getText());
		this.outputMessage.setText(encryptedText);
	}
	@Override
	public void decrypt() {
		if (!this.initializeCryptosystem())
			return;
		String decryptedText = this.encryptor.decrypt(this.inputMessage.getText());
		this.outputMessage.setText(decryptedText);
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputMessage.getText());
		this.outputMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Криптосистема Меркла-Хеллмана", "MerkleHellmanDescription.md");
	}
}
