package org.example.controllers.lab7;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.*;
import org.example.lab7.MerkleHellman;

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

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;

	private final MerkleHellman encryptor = new MerkleHellman();
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

	private Alphabet getSelectedAlphabet() {
		if (cyrillicRadioButton.isSelected())
			return USED_CYRILLIC_ALPHABET;
		else
			return USED_LATIN_ALPHABET;
	}

	public static final Alphabet USED_CYRILLIC_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	public static final Alphabet USED_LATIN_ALPHABET = AlphabetConstants.LATIN_WITH_SPACE;

	private boolean initializeCryptosystem() {
		Alphabet alphabet = this.getSelectedAlphabet();
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
		this.encryptor.setNewAlphabet(alphabet);
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
		System.out.println("Public key: " + this.encryptor.getPublicKeyString());
		System.out.println("Private key: " + this.encryptor.getPrivateKey());
	}
	@Override
	public void decrypt() {
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
