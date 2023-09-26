package org.example.lab2;

import org.example.common.Encryptor;
import org.example.lab1.GlobalVariables;

import java.util.ArrayList;
import java.util.Arrays;

public class VigenereEncryptor implements Encryptor {
	final static public ArrayList<Character> CYRILLIC_ALPHABET_LOWERCASE = new ArrayList<>(Arrays.asList(
			'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
	));
	final static public ArrayList<Character> CYRILLIC_ALPHABET_UPPERCASE = new ArrayList<>(Arrays.asList(
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'
	));

	public static void main(String[] args) {
		String key = "КЛЮЧ";
		String message = "ЗАМЕНА";
		VigenereEncryptor encryptor = new VigenereEncryptor(key);
		String encryptedMessage = encryptor.encrypt(message);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);
		System.out.println("Сообщение: " + message);
		System.out.println("Ключ: " + key);
		System.out.println("Зашифрованное сообщение: " + encryptedMessage);
		System.out.println("Расшифрованное сообщение: " + decryptedMessage);
	}
	private String key;
	private ArrayList<Integer> keyEffect;
	public VigenereEncryptor(String key) {
		this.key = key;
		this.keyEffect = this.generateKeyEffect();
	}
	private ArrayList<Integer> generateKeyEffect() {
		ArrayList<Integer> output = new ArrayList<>();
		for (int i = 0; i < this.key.length(); i++) {
			char letter = this.key.charAt(i);
			int position = Character.isUpperCase(letter) ? CYRILLIC_ALPHABET_UPPERCASE.indexOf(letter) : CYRILLIC_ALPHABET_LOWERCASE.indexOf(letter);
			position++; // для учёта начала считывания с 1-цы в алгоритме
			output.add(position);
		}
		return output;
	}
	@Override
	public String encrypt(String message) {
		StringBuilder output = new StringBuilder();
		int j = 0;
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean isUpper = Character.isUpperCase(letter);

			int oldLetterIndex = CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(letter)) + 1;
			int newLetterIndex = (oldLetterIndex + keyEffect.get(j++)) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			char newLetter = isUpper ? CYRILLIC_ALPHABET_UPPERCASE.get(newLetterIndex-1) :
									   CYRILLIC_ALPHABET_LOWERCASE.get(newLetterIndex-1);
			output.append(newLetter);

			if (j >= keyEffect.size())
				j = 0;
		}
		return output.toString();
	}
	@Override
	public String decrypt(String message) {
		StringBuilder output = new StringBuilder();
		int j = 0;
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean isUpper = Character.isUpperCase(letter);

			int oldLetterIndex = CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(letter)) + 1;
			int newLetterIndex = (oldLetterIndex - keyEffect.get(j++)) % GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			if (newLetterIndex < 0)
				newLetterIndex = GlobalVariables.CYRILLIC_ALPHABET_SIZE - Math.abs(newLetterIndex);
			char newLetter = isUpper ? CYRILLIC_ALPHABET_UPPERCASE.get(newLetterIndex-1) :
									   CYRILLIC_ALPHABET_LOWERCASE.get(newLetterIndex-1);
			output.append(newLetter);

			if (j >= keyEffect.size())
				j = 0;
		}
		return output.toString();
	}
}
