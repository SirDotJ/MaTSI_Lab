package org.example.lab2;

import org.example.common.Encryptor;
import org.example.common.GlobalVariables;

public class VigenereEncryptor implements Encryptor {
	public static void main(String[] args) { // тестирование работы класса
		String key = "ГОРНЫЙ";
		String message = "ВИЗУАЛЬНАЯ";
		VigenereEncryptor encryptor = new VigenereEncryptor(key);
		String encryptedMessage = encryptor.encrypt(message);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);
		System.out.println("Сообщение: " + message);
		System.out.println("Ключ: " + key);
		System.out.println("Зашифрованное сообщение: " + encryptedMessage);
		System.out.println("Расшифрованное сообщение: " + decryptedMessage);
	}
	private String key;
	public VigenereEncryptor(String key) {
		this.key = key;
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

		// Зашифровка сообщения по формуле Вижинера
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char messageLetter = message.charAt(i);
			boolean isUpper = Character.isUpperCase(messageLetter);

			int openMessageLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(messageLetter));
			int keyLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(appliedKey.charAt(i)));
			int encryptedLetterIndex = (openMessageLetterIndex + keyLetterIndex + 1) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
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

		// Расшифровка сообщения по преобразованной формуле Вижинера
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char encryptedMessageLetter = message.charAt(i);
			boolean isUpper = Character.isUpperCase(encryptedMessageLetter);

			int encryptedMessageLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(encryptedMessageLetter));
			int keyLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(appliedKey.charAt(i)));
			int openMessageLetterIndex = (encryptedMessageLetterIndex - keyLetterIndex - 1) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			if (openMessageLetterIndex < 0)
				openMessageLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_SIZE - Math.abs(openMessageLetterIndex);
			char newLetter = isUpper ? GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(openMessageLetterIndex) :
									   GlobalVariables.CYRILLIC_ALPHABET_LOWERCASE.get(openMessageLetterIndex);
			decryptedMessage.append(newLetter);
		}
		return decryptedMessage.toString();
	}
}
