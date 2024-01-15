package org.togu.lab2;

import org.togu.common.Alphabet;
import org.togu.common.AlphabetConstants;
import org.togu.common.Decryptor;
import org.togu.common.Encryptor;

public class VigenereEncryptor implements Encryptor, Decryptor {
	private static final Alphabet DEFAULT_ALPHABET = AlphabetConstants.FULL_WITH_SPACE;
	private static final String DEFAULT_KEY = "ТЕСТ";

	private final Alphabet alphabet;
	private String key;

	public VigenereEncryptor(Alphabet alphabet, String key) {
		this.alphabet = alphabet;
		this.key = key;
	}
	public VigenereEncryptor(Alphabet alphabet) {
		this(alphabet, DEFAULT_KEY);
	}
	public VigenereEncryptor(String key) {
		this(DEFAULT_ALPHABET, key);
	}
	public VigenereEncryptor() {
		this(DEFAULT_ALPHABET, DEFAULT_KEY);
	}

	public void setKey(String key) {
		this.key = key;
	}

	// Расширяет ключ для применения шифра над переданным сообщением добавляя себя на конец пока размер не совпадёт с сообщением
	private String fitKeyToMessage(String message) {
		StringBuilder keyBuilder = new StringBuilder(this.key);
		if (message.length() <= keyBuilder.length())
			return keyBuilder.toString();

		int currentSymbolIndex = 0;
		for (int i = 0; i < (message.length() - this.key.length()); i++) {
			keyBuilder.append(this.key.charAt(currentSymbolIndex++));
			if (currentSymbolIndex >= this.key.length())
				currentSymbolIndex = 0;
		}

		return keyBuilder.toString();
	}

	@Override
	public String encrypt(String message) {
		// Расширение ключа шифра
		String appliedKey = this.fitKeyToMessage(message);

		// Зашифровка сообщения по формуле Вижинера
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char messageLetter = message.charAt(i);
			char keyLetter = appliedKey.charAt(i);

			int openMessageLetterIndex = this.alphabet.indexOf(messageLetter);
			int keyLetterIndex = this.alphabet.indexOf(keyLetter);
			int encryptedLetterIndex = (openMessageLetterIndex + keyLetterIndex + 1) % this.alphabet.size();

			char newLetter = this.alphabet.get(encryptedLetterIndex);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}
	@Override
	public String decrypt(String message) {
		// Расширение ключа шифра
		String appliedKey = this.fitKeyToMessage(message);

		// Расшифровка сообщения по преобразованной формуле Вижинера
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char encryptedMessageLetter = message.charAt(i);
			char keyLetter = appliedKey.charAt(i);

			int encryptedMessageLetterIndex = this.alphabet.indexOf(encryptedMessageLetter);
			int keyLetterIndex = this.alphabet.indexOf(keyLetter);
			int openMessageLetterIndex = (encryptedMessageLetterIndex - keyLetterIndex - 1) % this.alphabet.size();
			if (openMessageLetterIndex < 0)
				openMessageLetterIndex = this.alphabet.size() - Math.abs(openMessageLetterIndex);

			char newLetter = this.alphabet.get(openMessageLetterIndex);
			decryptedMessage.append(newLetter);
		}
		return decryptedMessage.toString();
	}
}
