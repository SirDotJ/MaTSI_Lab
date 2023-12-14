package org.example.lab2;

import org.example.common.Alphabet;
import org.example.common.Decryptor;
import org.example.common.Encryptor;
import org.example.common.AlphabetConstants;

// Для реализации была взята первая формула mod N(k_i - X_i)
public class BofortEncryptor implements Encryptor, Decryptor {
	public enum VARIANT {
		ONE, // Y_i = (k_i - X_i)mod N
		TWO // Y_i = (X_i - k_i)mod N
	}

	private static final Alphabet DEFAULT_ALPHABET = AlphabetConstants.FULL_WITH_SPACE;
	private static final String DEFAULT_KEY = "ТЕСТ";
	private static final VARIANT DEFAULT_VARIANT = VARIANT.ONE;

	private final Alphabet alphabet;
	private String key;
	private VARIANT variant;

	public BofortEncryptor(Alphabet alphabet, String key, VARIANT variant) {
		this.alphabet = alphabet;
		this.key = key;
		this.variant = variant;
	}
	public BofortEncryptor() {
		this(DEFAULT_ALPHABET, DEFAULT_KEY, DEFAULT_VARIANT);
	}
	public BofortEncryptor(String key, VARIANT variant) {
		this(DEFAULT_ALPHABET, key, variant);
	}
	public BofortEncryptor(Alphabet alphabet, VARIANT variant) {
		this(alphabet, DEFAULT_KEY, variant);
	}
	public BofortEncryptor(Alphabet alphabet, String key) {
		this(alphabet, key, DEFAULT_VARIANT);
	}
	public BofortEncryptor(Alphabet alphabet) {
		this(alphabet, DEFAULT_KEY, DEFAULT_VARIANT);
	}
	public BofortEncryptor(String key) {
		this(DEFAULT_ALPHABET, key, DEFAULT_VARIANT);
	}
	public BofortEncryptor(VARIANT variant) {
		this(DEFAULT_ALPHABET, DEFAULT_KEY, variant);
	}

	public void setKey(String key) {
		this.key = key;
	}
	public void setVariant(VARIANT variant) {
		this.variant = variant;
	}

	// Расширяет ключ для применения шифра над переданным сообщением добавляя себя на конец пока размер не совпадёт с сообщением
	private String fitKeyToMessage(String message) {
		StringBuilder keyBuilder = new StringBuilder(this.key);
		if (message.length() <= keyBuilder.length())
			return keyBuilder.toString();

		int messageToKeyDifference = message.length() - this.key.length();
		int currentKeySymbolIndex = 0;
		for (int i = 0; i < messageToKeyDifference; i++) {
			keyBuilder.append(this.key.charAt(currentKeySymbolIndex++));
			if (currentKeySymbolIndex >= this.key.length())
				currentKeySymbolIndex = 0;
		}

		return keyBuilder.toString();
	}

	@Override
	public String encrypt(String message) {
		// Расширение ключа шифра
		String appliedKey = this.fitKeyToMessage(message);

		// Зашифровка сообщения по формуле Бофорта
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char messageLetter = message.charAt(i);
			char keyLetter = appliedKey.charAt(i);

			int openMessageLetterIndex = this.alphabet.indexOf(messageLetter);
			int keyLetterIndex = this.alphabet.indexOf(keyLetter);
			int encryptedLetterIndex;
			switch (this.variant) {
				case TWO -> encryptedLetterIndex = (openMessageLetterIndex - keyLetterIndex - 1) % this.alphabet.size();
				default -> encryptedLetterIndex = (keyLetterIndex - openMessageLetterIndex - 1) % this.alphabet.size();
			}
			if (encryptedLetterIndex < 0)
				encryptedLetterIndex = this.alphabet.size() - Math.abs(encryptedLetterIndex);

			char newLetter = this.alphabet.get(encryptedLetterIndex);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}

	@Override
	public String decrypt(String message) {
		// Расширение ключа шифра
		String appliedKey = this.fitKeyToMessage(message);

		// Расшифровка сообщения по преобразованной формуле Бофорта
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char messageLetter = message.charAt(i);
			char keyLetter = appliedKey.charAt(i);

			int encryptedMessageLetterIndex = this.alphabet.indexOf(messageLetter);
			int keyLetterIndex = this.alphabet.indexOf(keyLetter);
			int openMessageLetterIndex;
			switch (this.variant) {
				case TWO -> openMessageLetterIndex = (encryptedMessageLetterIndex + keyLetterIndex + 1) % this.alphabet.size();
				default -> openMessageLetterIndex = (keyLetterIndex - encryptedMessageLetterIndex - 1) % this.alphabet.size();
			}
			if (openMessageLetterIndex < 0)
				openMessageLetterIndex = this.alphabet.size() - Math.abs(openMessageLetterIndex);

			char newLetter = this.alphabet.get(openMessageLetterIndex);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}
}
