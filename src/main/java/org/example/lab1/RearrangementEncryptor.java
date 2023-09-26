package org.example.lab1;

import org.example.common.Encryptor;

import java.util.ArrayList;
import java.util.Arrays;

public class RearrangementEncryptor implements Encryptor {
	final private ArrayList<Integer> key1;
	final private ArrayList<Integer> key2;
	private ArrayList<String> encryptionTable; // используется для отображения алгоритма шифровки
	private ArrayList<String> decryptionTable; // используется для отображения алгоритма расшифровки (перевернут для чтения столбцов как строки в ArrayList<String>)
	public RearrangementEncryptor(ArrayList<Integer> key1, ArrayList<Integer> key2) {
		// Копии делаются для дальнейших манипуляций без изменения оригинала
		this.key1 = new ArrayList<>(key1);
		this.key2 = new ArrayList<>(key2);
		// Манипуляции ниже проводятся, если переданные ключи не начинают индекс с нуля
		if(!this.key1.contains(0))
			this.key1.replaceAll(integer -> integer - 1);
		if(!this.key2.contains(0))
			this.key2.replaceAll(integer -> integer - 1);
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
				newMessage.append(String.valueOf(GlobalVariables.SPACE_CHARACTER).repeat(additionalSpaceCount));
			}
			else
				for (int i = 0; i < difference; i++)
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
		// Возвращаем шифрованное сообщение
		return encryptedMessage.toString();
	}

	@Override
	public String decrypt(String message) throws IllegalArgumentException {
		int messageLength = message.length();
		int rowCount = this.key1.size();
		int columnCount = this.key2.size();

		// Не принимаем сообщения неправильной размерности
		if(messageLength != rowCount * columnCount)
			throw new IllegalArgumentException();

		// Заполнение перевернутой таблицы расшифровки по ключу k2 (переворот производится в реализации для облегчения работы с ArrayList<String>)
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
		// Возвращаем расшифрованное сообщение
		return decryptedMessage.toString();
	}

	public void printEncryptionTable() {
		if(this.encryptionTable == null || this.encryptionTable.isEmpty()) {
			System.out.println("Ошибка: таблица шифровки пуста");
			return;
		}
		encryptionTable.forEach((subString) -> {
			System.out.print('|');
			for (int i = 0; i < subString.length(); i++) {
				System.out.print(subString.charAt(i) + "|");
			}
			System.out.println();
		});
	}
	public void printDecryptionTable() {
		if(this.decryptionTable == null || this.decryptionTable.isEmpty()) {
			System.out.println("Ошибка: таблица расшифровки пуста");
			return;
		}
		decryptionTable.forEach((subString) -> {
			System.out.print('|');
			for (int i = 0; i < subString.length(); i++) {
				System.out.print(subString.charAt(i) + "|");
			}
			System.out.println();
		});
	}
	public String getEncryptionTable() {
		if(this.encryptionTable == null || this.encryptionTable.isEmpty()) {
			return "Таблицы зашифровки нет";
		}
		StringBuilder builder = new StringBuilder();
		encryptionTable.forEach((subString) -> {
			builder.append('|');
			for (int i = 0; i < subString.length(); i++) {
				builder.append(subString.charAt(i) + "|");
			}
			builder.append("\n");
		});
		return builder.toString();
	}
	public String getDecryptionTable() {
		if (this.decryptionTable == null || this.decryptionTable.isEmpty()) {
			return "Таблицы расшифровки нет";
		}
		StringBuilder builder = new StringBuilder();
		decryptionTable.forEach((subString) -> {
			builder.append('|');
			for (int i = 0; i < subString.length(); i++) {
				builder.append(subString.charAt(i) + "|");
			}
			builder.append("\n");
		});
		return builder.toString();
	}
}
