package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.*;
import org.example.lab6.RSA;

public class RSAController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField prime1InputTextField;
	@FXML
	TextField prime2InputTextField;
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputMessage;

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;

	private final RSA encryptor = new RSA();

	private int getPrime1() {
		return Integer.parseInt(prime1InputTextField.getText());
	}

	private int getPrime2() {
		return Integer.parseInt(prime2InputTextField.getText());
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
		int prime1 = this.getPrime1();
		int prime2 = this.getPrime2();
		Alphabet alphabet = this.getSelectedAlphabet();
		try {
			this.encryptor.setNewKey(prime1, prime2);
		} catch (IllegalArgumentException e) {
			Alerts.showError(
					"Недопустимые ключи",
					"Ключи неправильные",
					"Пожалуйста введите два простых числа"
			);
			return false;
		}
		this.encryptor.setNewAlphabet(alphabet);
		return true;
	}

	public void generateKeys() {
		int prime1 = PrimeMath.getRandomPrime();
		int prime2;
		do {
			prime2 = PrimeMath.getRandomPrime();
		} while (prime1 == prime2);

		this.prime1InputTextField.setText(String.valueOf(prime1));
		this.prime2InputTextField.setText(String.valueOf(prime2));
	}

	@Override
	public void encrypt() {
		if (!this.initializeCryptosystem())
			return;
		String encryptedText = this.encryptor.encrypt(this.inputMessage.getText());
		this.outputMessage.setText(encryptedText);
		System.out.println("Public key: " + this.encryptor.getPublicKey());
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
		return;
	}
}
