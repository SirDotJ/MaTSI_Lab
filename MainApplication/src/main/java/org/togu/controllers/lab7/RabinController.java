package org.togu.controllers.lab7;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.togu.common.*;
import org.togu.controllers.HelpController;
import org.togu.lab7.Rabin;

public class RabinController implements EncryptorForm, DecryptorForm {
	@FXML
	TextField prime1InputTextField;
	@FXML
	TextField prime2InputTextField;
	@FXML
	TextField markerInputTextField;

	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputMessage;

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;
	@FXML RadioButton enRuRadioButton;

	private final Rabin encryptor = new Rabin();

	private int getPrime1() {
		return Integer.parseInt(prime1InputTextField.getText());
	}

	private int getPrime2() {
		return Integer.parseInt(prime2InputTextField.getText());
	}
	private int getMarker() {
		return Integer.parseInt(this.markerInputTextField.getText());
	}

	private Alphabet getSelectedAlphabet() {
		if (cyrillicRadioButton.isSelected())
			return USED_CYRILLIC_ALPHABET;
		else if (latinRadioButton.isSelected())
			return USED_LATIN_ALPHABET;
		else
			return USED_ALL_ALPHABET;
	}

	public static final Alphabet USED_CYRILLIC_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	public static final Alphabet USED_LATIN_ALPHABET = AlphabetConstants.LATIN_WITH_SPACE;
	public static final Alphabet USED_ALL_ALPHABET = AlphabetConstants.FULL_KEYBOARD;

	private boolean initializeCryptosystem() {
		int prime1 = this.getPrime1();
		int prime2 = this.getPrime2();
		int marker = this.getMarker();
		Alphabet alphabet = this.getSelectedAlphabet();
		try {
			this.encryptor.setNewKey(prime1, prime2, marker);
		} catch (IllegalArgumentException e) {
			Alerts.showError(
					"Недопустимые ключи",
					"Ключи неправильные",
					"Пожалуйста введите два простых числа, для которых p mod 4 = 3 и q mod 4 = 3"
			);
			return false;
		}
		this.encryptor.setNewAlphabet(alphabet);
		return true;
	}

	public void generateKeys() {
		int prime1;
		int prime2;
		do {
			prime1 = PrimeMath.getRandomPrime();
			prime2 = PrimeMath.getRandomPrime();
		} while (prime1 == prime2 ||
				(prime1 % 4 != 3) ||
				(prime2 % 4 != 3));

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
		HelpController.open("Криптосистема Рабина", "RabinDescription.md");
	}
}
