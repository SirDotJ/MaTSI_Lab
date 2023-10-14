package org.example.lab1;

import org.example.common.Decryptor;
import org.example.common.Encryptor;
import org.example.common.AlphabetConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RearrangementEncryptor implements Encryptor, Decryptor {
	static final List<Integer> DEFAULT_KEY_1 = new ArrayList<>(Arrays.asList(
			3, 1, 4, 2
	));
	static final List<Integer> DEFAULT_KEY_2 = new ArrayList<>(Arrays.asList(
			1, 5, 2, 4, 3
	));

	private List<Integer> key1;
	private List<Integer> key2;
	private List<String> encryptionTable; // используется для отображения алгоритма шифровки
	private List<String> decryptionTable; // используется для отображения алгоритма расшифровки (перевернут для чтения столбцов как строки в ArrayList<String>)
	public RearrangementEncryptor(List<Integer> key1, List<Integer> key2) {
		this.setKey1(key1);
		this.setKey2(key2);
	}
	public RearrangementEncryptor() {
		this(DEFAULT_KEY_1, DEFAULT_KEY_2);
	}

	public void setKey1(List<Integer> key1) {
		List<Integer> keyCopy = new ArrayList<>(key1);
		if(!keyCopy.contains(0))
			keyCopy.replaceAll(integer -> integer - 1);
		this.key1 = keyCopy;
	}

	public void setKey2(List<Integer> key2) {
		List<Integer> keyCopy = new ArrayList<>(key2);
		if(!keyCopy.contains(0))
			keyCopy.replaceAll(integer -> integer - 1);
		this.key2 = keyCopy;
	}

	public String getEncryptionTable() throws IllegalStateException {
		if(this.encryptionTable == null || this.encryptionTable.isEmpty())
			throw new IllegalStateException("No encryption table is present");

		StringBuilder builder = new StringBuilder();
		encryptionTable.forEach((subString) -> {
			builder.append('|');
			for (int i = 0; i < subString.length(); i++) {
				builder.append(subString.charAt(i)).append("|");
			}
			builder.append("\n");
		});

		return builder.toString();
	}
	public String getDecryptionTable() throws IllegalStateException {
		if (this.decryptionTable == null || this.decryptionTable.isEmpty())
			throw new IllegalStateException("No decryption table is present");

		StringBuilder builder = new StringBuilder();
		decryptionTable.forEach((subString) -> {
			builder.append('|');
			for (int i = 0; i < subString.length(); i++) {
				builder.append(subString.charAt(i)).append("|");
			}
			builder.append("\n");
		});

		return builder.toString();
	}

	@Override
	public String encrypt(String message) {
		int messageLength = message.length();
		int rowCount = this.key1.size();
		int columnCount = this.key2.size();

		// Проверка корректного размера сообщения
		if (messageLength % rowCount != 0) {
			StringBuilder newMessage = new StringBuilder(message);
			int difference = messageLength - rowCount * columnCount;
			if (difference < 0 || difference > 1) {
				int additionalSpaceCount = difference < 0 ? -difference : columnCount - difference;
				newMessage.append(String.valueOf(AlphabetConstants.SPACE_CHARACTER).repeat(additionalSpaceCount));
			}
			else for (int i = 0; i < difference; i++)
				newMessage.deleteCharAt(newMessage.length() - 1);
			message = newMessage.toString();
		}

		// Заполнение таблицу шифровки по ключу k1
		this.encryptionTable = new ArrayList<>(Arrays.asList(new String[rowCount]));
		for (int i = 0; i < rowCount; i++) {
			int substringRowInsertIndex = this.key1.get(i);
			int startIndex = i * columnCount;
			int endIndex = startIndex + columnCount;
			String subString = message.substring(startIndex, endIndex);
			this.encryptionTable.set(substringRowInsertIndex, subString);
		}

		// Чтение из таблицы шифровки по ключу k2
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < columnCount; i++) {
			int columnReadIndex = this.key2.get(i);
			for (int j = 0; j < rowCount; j++) {
				String subString = this.encryptionTable.get(j);
				char letter = subString.charAt(columnReadIndex);
				encryptedMessage.append(letter);
			}
		}

		return encryptedMessage.toString();
	}
	@Override
	public String decrypt(String message) throws IllegalArgumentException {
		int messageLength = message.length();
		int rowCount = this.key1.size();
		int columnCount = this.key2.size();

		// Не принимаем сообщения неправильной размерности
		if(messageLength != rowCount * columnCount)
			throw new IllegalArgumentException("Passed message does not fit to keys");

		// Заполнение перевернутой таблицы расшифровки по ключу k2 (переворот производится в реализации для облегчения работы с List<String>)
		this.decryptionTable = new ArrayList<>(Arrays.asList(new String[columnCount]));
		for (int i = 0; i < columnCount; i++) {
			int substringColumnInsertIndex = this.key2.get(i);
			int startIndex = i * rowCount;
			int endIndex = startIndex + rowCount;
			String subString = message.substring(startIndex, endIndex);
			this.decryptionTable.set(substringColumnInsertIndex, subString);
		}

		// Чтение из перевернутой таблицы расшифровки по ключу k1
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < rowCount; i++) {
			int rowReadIndex = this.key1.get(i);
			for (int j = 0; j < columnCount; j++) {
				decryptedMessage.append(this.decryptionTable.get(j).charAt(rowReadIndex));
			}
		}

		return decryptedMessage.toString();
	}
}
