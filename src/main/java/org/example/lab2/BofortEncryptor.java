package org.example.lab2;

import org.example.common.Encryptor;

import java.util.ArrayList;
import java.util.Arrays;

// Для реализации была взята первая формула mod N(k_i - X_i)
public class BofortEncryptor implements Encryptor {
	final static private ArrayList<Integer> TEST_KEY = new ArrayList<>(Arrays.asList(
		18, 1, 4, 33, 19, 32, 28, 10, 7, 29, 31, 0, 8, 11, 21, 13, 20, 27, 17, 25, 3, 15, 26, 22, 24, 12, 16, 9, 6, 23, 2, 5, 30, 14
	));
	final static public ArrayList<Character> CYRILLIC_ALPHABET_LOWERCASE = new ArrayList<>(Arrays.asList(
			'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '_'
	));
	final static public ArrayList<Character> CYRILLIC_ALPHABET_UPPERCASE = new ArrayList<>(Arrays.asList(
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', '_'
	));
	private ArrayList<Character> encryptedCyrillicAlphabetUppercase;
	private ArrayList<Character> encryptedCyrillicAlphabetLowercase;
	public static void main(String[] args) {
		if (!checkKeyValidity(TEST_KEY)) {
			System.out.println("Ошибка: TEST_KEY - не корректный ключ, поменяйте его значения и попробуйте снова");
			return;
		}
		BofortEncryptor encryptor = new BofortEncryptor(TEST_KEY);
		String message = "СООБЩЕНИЕ_ПОЛЬЗОВАТЕЛЮ";
		String encryptedMessage = encryptor.encrypt(message);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);
		System.out.println("Сообщение: " + message);
		System.out.println("Зашифрованное сообщение: " + encryptedMessage);
		System.out.println("Расшифрованное сообщение: " + decryptedMessage);
		System.out.println("Обычный алфавит:");
		System.out.println(encryptor.getOriginalAlphabetUppercase());
		System.out.println("Зашифрованный алфавит:");
		System.out.println(encryptor.getEncryptedAlphabetUppercase());
	}
	private ArrayList<Integer> key;
	public BofortEncryptor(ArrayList<Integer> key) {
		this.key = key;
		this.encryptedCyrillicAlphabetUppercase = calculateEncryptedAlphabetUppercase(this.key);
		this.encryptedCyrillicAlphabetLowercase = calculateEncryptedAlphabetLowercase(this.key);
	}
	static private ArrayList<Character> calculateEncryptedAlphabetUppercase(ArrayList<Integer> key) {
		ArrayList<Character> output = new ArrayList<>();
		for (int i = 0; i < CYRILLIC_ALPHABET_UPPERCASE.size(); i++) {
			int oldIndex = i;
			int keyValue = key.get(i);
			int newIndex = (keyValue - oldIndex) % CYRILLIC_ALPHABET_UPPERCASE.size();
			if (newIndex < 0)
				newIndex = CYRILLIC_ALPHABET_UPPERCASE.size() - Math.abs(newIndex);
			output.add(CYRILLIC_ALPHABET_UPPERCASE.get(newIndex));
		}
		return output;
	}
	static private ArrayList<Character> calculateEncryptedAlphabetLowercase(ArrayList<Integer> key) {
		ArrayList<Character> output = new ArrayList<>();
		for (int i = 0; i < CYRILLIC_ALPHABET_LOWERCASE.size(); i++) {
			int oldIndex = i;
			int keyValue = key.get(i);
			int newIndex = (keyValue - oldIndex) % CYRILLIC_ALPHABET_LOWERCASE.size();
			if (newIndex < 0)
				newIndex = CYRILLIC_ALPHABET_LOWERCASE.size() - Math.abs(newIndex);
			output.add(CYRILLIC_ALPHABET_LOWERCASE.get(newIndex));
		}
		return output;
	}
	@Override
	public String encrypt(String message) {
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean isUpper = Character.isUpperCase(letter);
			int oldIndex = CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(letter));
			char newLetter = isUpper ? this.encryptedCyrillicAlphabetUppercase.get(oldIndex)
									 : this.encryptedCyrillicAlphabetLowercase.get(oldIndex);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}

	@Override
	public String decrypt(String message) {
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean isUpper = Character.isUpperCase(letter);
			int oldIndex = CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(letter));
			int keyValue = this.key.indexOf(oldIndex);
			int newIndex = (keyValue - oldIndex) % CYRILLIC_ALPHABET_UPPERCASE.size();
			if (newIndex < 0)
				newIndex = CYRILLIC_ALPHABET_UPPERCASE.size() - Math.abs(newIndex);
			char newLetter = isUpper ? this.CYRILLIC_ALPHABET_UPPERCASE.get(newIndex)
									 : this.CYRILLIC_ALPHABET_LOWERCASE.get(newIndex);
			decryptedMessage.append(newLetter);
		}
		return decryptedMessage.toString();
	}
	private static boolean checkKeyValidity(ArrayList<Integer> key) {
		ArrayList<Integer> seenNumbers = new ArrayList<>();
		for (int number : key) {
			if (seenNumbers.contains(number))
				return false;
			else
				seenNumbers.add(number);
		}
		return true;
	}
	public String getOriginalAlphabetUppercase() {
		StringBuilder output = new StringBuilder();
		for (Character character : CYRILLIC_ALPHABET_UPPERCASE)
			output.append(character);
		return output.toString();
	}
	public String getOriginalAlphabetLowercase() {
		StringBuilder output = new StringBuilder();
		for (Character character : CYRILLIC_ALPHABET_LOWERCASE)
			output.append(character);
		return output.toString();
	}
	public String getEncryptedAlphabetUppercase() {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < CYRILLIC_ALPHABET_UPPERCASE.size(); i++) {
			int oldIndex = i;
			int newIndex = (this.key.get(i) - oldIndex) % CYRILLIC_ALPHABET_UPPERCASE.size();
			if (newIndex < 0)
				newIndex = CYRILLIC_ALPHABET_UPPERCASE.size() - Math.abs(newIndex);
			output.append(CYRILLIC_ALPHABET_UPPERCASE.get(newIndex));
		}
		return output.toString();
	}
	public String getEncryptedAlphabetLowercase() {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < CYRILLIC_ALPHABET_LOWERCASE.size(); i++) {
			int oldIndex = i;
			int newIndex = (this.key.get(i) - oldIndex) % CYRILLIC_ALPHABET_LOWERCASE.size();
			if (newIndex < 0)
				newIndex = CYRILLIC_ALPHABET_LOWERCASE.size() - Math.abs(newIndex);
			output.append(CYRILLIC_ALPHABET_LOWERCASE.get(newIndex));
		}
		return output.toString();
	}
}
