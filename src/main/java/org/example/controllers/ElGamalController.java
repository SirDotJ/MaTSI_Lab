package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.*;
import org.example.lab6.ElGamal;
import org.example.lab6.User;

import java.math.BigInteger;
import java.util.List;

public class ElGamalController {
	@FXML
	TextField prime1InputTextField;
	@FXML
	TextField prime2InputTextField;
	@FXML
	TextField userCountInputTextField;
	@FXML
	TextArea message;
	@FXML
	TextField senderIdInput;
	@FXML
	TextArea userListOutput;
	@FXML
	TextArea logOutput;

	@FXML
	private RadioButton cyrillicRadioButton;
	@FXML
	private RadioButton latinRadioButton;

	private final ElGamal encryptor = new ElGamal();
	private int getPrime1() {
		return Integer.parseInt(this.prime1InputTextField.getText());
	}
	private int getPrime2() {
		return Integer.parseInt(this.prime2InputTextField.getText());
	}
	private int getUserCount() {
		return Integer.parseInt(this.userCountInputTextField.getText());
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
		int userCount = this.getUserCount();
		Alphabet alphabet = this.getSelectedAlphabet();
		try {
			this.encryptor.setNewKey(prime1, prime2, userCount);
		} catch (IllegalArgumentException e) {
			Alerts.showError(
					"Недопустимые ключи",
					"Ключи неправильные",
					"Пожалуйста введите два взаимно простых числа"
			);
			return false;
		}
		this.encryptor.setNewAlphabet(alphabet);

		this.userListOutput.setText("");
		for (User user : this.encryptor.getAllUsers())
			this.userListOutput.appendText(user + "\n");

		return true;
	}

	public void generateUsers() {
		if (!this.initializeCryptosystem())
			return;

		this.userListOutput.setText("");
		for (User user : this.encryptor.getAllUsers())
			this.userListOutput.appendText(user + "\n");
	}

	public void generateKeys() {
		int prime1 = PrimeMath.getRandomPrime();
		int prime2;
		do {
			prime2 = PrimeMath.getRandomPrime();
		} while (prime1 == prime2 ||
				(!PrimeMath.primesToEachOther(prime1, prime2)) ||
				(!modCheck(prime1, prime2))
		);

		this.prime1InputTextField.setText(String.valueOf(prime1));
		this.prime2InputTextField.setText(String.valueOf(prime2));
	}

	private static boolean modCheck(int prime1, int prime2) {
		BigInteger bigKey1 = BigInteger.valueOf(prime1);
        BigInteger bigKey2 = BigInteger.valueOf(prime2);

        return (bigKey2.pow(prime1).mod(bigKey1).equals(bigKey2));
	}

	public void sendToAll() {
		int senderId;
		try {
			senderId = Integer.parseInt(this.senderIdInput.getText());
		} catch (IllegalArgumentException e) {
			Alerts.showError(
					"Неизвестный пользователь",
					"Пользователь не был найден",
					"Пожалуйста введите id существующего пользователя в сети"
			);
			return;
		}

		User sender = this.encryptor.getUser(senderId);
		String message = this.message.getText();

		this.logOutput.appendText("User №" + senderId + " sent message: \"" + message + "\" to all users;\n");

		List<String> encryptedMessages = this.encryptor.massSend(senderId, message);
		int counter = 0;
		for (int i = 0; i < encryptor.getAllUsers().size(); i++) {
			if (i == senderId)
				continue;
			String encryptedMessage = encryptedMessages.get(counter++);
			this.logOutput.appendText("User №" + i + " received encrypted message: \"" + encryptedMessage + "\";\n");
		}

		List<String> decryptedMessages = this.encryptor.massReceive(senderId, encryptedMessages);
		counter = 0;
		for (int i = 0; i < encryptor.getAllUsers().size(); i++) {
			if (i == senderId)
				continue;
			String decryptedMessage = decryptedMessages.get(counter++);
			this.logOutput.appendText("User №" + i + " decrypted message: \"" + decryptedMessage + "\";\n");
		}
	}

	public void clearLog() {
		this.logOutput.setText("");
	}

	public void openHelp() {
		return;
	}
}
