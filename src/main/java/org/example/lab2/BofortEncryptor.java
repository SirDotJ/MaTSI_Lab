package org.example.lab2;

import org.example.common.Encryptor;
import org.example.common.GlobalVariables;

// Для реализации была взята первая формула mod N(k_i - X_i)
public class BofortEncryptor implements Encryptor {
	public static enum VARIANT {
		ONE, // Y_i = (k_i - X_i)mod N
		TWO // Y_i = (X_i - k_i)mod N
	}
	final static private String TEST_KEY = "КЛЮЧ";
	public static void main(String[] args) { // Тестирование работы класса
		String message = "СООБЩЕНИЕПОЛЬЗОВАТЕЛЮ";
		String key = TEST_KEY;
		System.out.println("Сообщение: " + message);
		System.out.println("Ключ: " + key);
		// Проверка формы №1
		BofortEncryptor encryptorOne = new BofortEncryptor(key, VARIANT.ONE);
		String encryptedMessageOne = encryptorOne.encrypt(message);
		String decryptedMessageOne = encryptorOne.decrypt(encryptedMessageOne);
		System.out.println("Вариант №1:");
		System.out.println("Сообщение: " + message);
		System.out.println("Зашифрованное сообщение: " + encryptedMessageOne);
		System.out.println("Расшифрованное сообщение: " + decryptedMessageOne);

		// Проверка формы №2
		BofortEncryptor encryptorTwo = new BofortEncryptor(key, VARIANT.TWO);
		String encryptedMessageTwo = encryptorTwo.encrypt(message);
		String decryptedMessageTwo = encryptorTwo.decrypt(encryptedMessageTwo);
		System.out.println("Вариант №1:");
		System.out.println("Сообщение: " + message);
		System.out.println("Зашифрованное сообщение: " + encryptedMessageTwo);
		System.out.println("Расшифрованное сообщение: " + decryptedMessageTwo);
	}
	private String key;
	private VARIANT variant;
	public BofortEncryptor(String key) {
		this(key, VARIANT.ONE);
	}
	public BofortEncryptor(String key, VARIANT variant) {
		this.key = key;
		this.variant = variant;
	}
	// Расширяет ключ для применения шифра над переданным сообщением добавляя себя на конец пока размер не совпадёт с сообщением
	private String fitKeyToMessage(String message) {
		StringBuilder keyBuilder = new StringBuilder(this.key);
		if (message.length() > keyBuilder.length()) {
			int currentSymbolIndex = 0;
			for (int i = 0; i < (message.length() - this.key.length()); i++) {
				keyBuilder.append(this.key.charAt(currentSymbolIndex++));
				if (currentSymbolIndex >= this.key.length())
					currentSymbolIndex = 0;
			}
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
			boolean isUpper = Character.isUpperCase(messageLetter);

			int openMessageLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(messageLetter));
			int keyLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(appliedKey.charAt(i)));
			int encryptedLetterIndex;
			switch (this.variant) {
				case TWO -> encryptedLetterIndex = (openMessageLetterIndex - keyLetterIndex) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
				default -> encryptedLetterIndex = (keyLetterIndex - openMessageLetterIndex) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			}
			if (encryptedLetterIndex < 0)
				encryptedLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_SIZE - Math.abs(encryptedLetterIndex);
			char newLetter = isUpper ? GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(encryptedLetterIndex) :
									   GlobalVariables.CYRILLIC_ALPHABET_LOWERCASE.get(encryptedLetterIndex);
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
			boolean isUpper = Character.isUpperCase(messageLetter);

			int encryptedMessageLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(messageLetter));
			int keyLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(appliedKey.charAt(i)));
			int openMessageLetterIndex;
			switch (this.variant) {
				case TWO -> openMessageLetterIndex = (encryptedMessageLetterIndex + keyLetterIndex) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
				default -> openMessageLetterIndex = (keyLetterIndex - encryptedMessageLetterIndex) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			}
			if (openMessageLetterIndex < 0)
				openMessageLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_SIZE - Math.abs(openMessageLetterIndex);
			char newLetter = isUpper ? GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(openMessageLetterIndex) :
									   GlobalVariables.CYRILLIC_ALPHABET_LOWERCASE.get(openMessageLetterIndex);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}
}
