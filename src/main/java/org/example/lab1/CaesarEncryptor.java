package org.example.lab1;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;
import org.example.common.Decryptor;
import org.example.common.Encryptor;

// Класс определяет алгоритм Цезаря (работает только с кириллицей)
public class CaesarEncryptor implements Encryptor, Decryptor {
	private static final Alphabet DEFAULT_ALPHABET = AlphabetConstants.FULL_WITH_SPACE;
	private static final int DEFAULT_KEY_VALUE = 0;

	private Alphabet alphabet;
	private int key;

	public CaesarEncryptor(Alphabet alphabet, int key) {
		this.alphabet = alphabet;
		this.key = key;
	}
	public CaesarEncryptor() {
		this(DEFAULT_ALPHABET, DEFAULT_KEY_VALUE);
	}
	public CaesarEncryptor(Alphabet alphabet) {
		this(alphabet, DEFAULT_KEY_VALUE);
	}
	public CaesarEncryptor(int key) {
		this(DEFAULT_ALPHABET, key);
	}

	public void setKey(int key) {
		if (Math.abs(key) > this.alphabet.size())
			key = key % this.alphabet.size();
		this.key = key;
	}

	public void setNewAlphabet(Alphabet newAlphabet) {
		this.alphabet = newAlphabet;
	}

	@Override
	public String encrypt(String message) {
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char messageLetter = message.charAt(i);

			int originalPosition = this.alphabet.indexOf(messageLetter);
			if(originalPosition == -1) { // если пробелы или другие символы вне алфавита
				encryptedMessage.append(messageLetter);
				continue;
			}

			int newPosition = originalPosition + this.key;
			if(newPosition > this.alphabet.size() - 1)
				newPosition -= this.alphabet.size();

			encryptedMessage.append(this.alphabet.get(newPosition));
		}
		return encryptedMessage.toString();
	}
	@Override
	public String decrypt(String message) {
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);

			int originalPosition = this.alphabet.indexOf(letter);
			if(originalPosition == -1) { // если пробелы или другие символы вне алфавита
				decryptedMessage.append(letter);
				continue;
			}

			int newPosition = originalPosition - this.key;
			if (newPosition < 0)
				newPosition = this.alphabet.size() - Math.abs(newPosition);
			decryptedMessage.append(this.alphabet.get(newPosition));
		}
		return decryptedMessage.toString();
	}
}
