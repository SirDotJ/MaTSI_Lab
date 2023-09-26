package org.example.lab1;

import java.util.ArrayList;
import java.util.Arrays;

import org.example.common.Encryptor;

// Класс определяет алгоритм Цезаря (работает только с кириллицей)
public class CaesarEncryptor implements Encryptor {
	/* Алфавиты сохранены в виде массивов так как строчные и прописные буквы 'Ё' не входят в общее правило расположения алфавита по порядку в UTF-8,
	из-за этого использование UTF-8 кода приведёт к усложнению алгоритма метода Цезаря, массивы посчитал достойной альтернативой*/
	final static public ArrayList<Character> CYRILLIC_ALPHABET_LOWERCASE = new ArrayList<>(Arrays.asList(
			'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
	));
	final static public ArrayList<Character> CYRILLIC_ALPHABET_UPPERCASE = new ArrayList<>(Arrays.asList(
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'
	));
	private final int key;
	public CaesarEncryptor(int key) {
		// Проверка на ключ со значением большего размера алфавита и его соответствующее преобразование
		if (Math.abs(key) > CYRILLIC_ALPHABET_LOWERCASE.size())
			key = key % CYRILLIC_ALPHABET_LOWERCASE.size();
		this.key = key;
	}
	@Override
	public String encrypt(String message) {
		StringBuilder decryptedMessage = new StringBuilder();
		int alphabetRightOffset = this.key; // переменная объявляется для повышения читаемости
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean isLowerCase = Character.isLowerCase(letter);
			int originalPosition = isLowerCase ? CYRILLIC_ALPHABET_LOWERCASE.indexOf(letter) :
												 CYRILLIC_ALPHABET_UPPERCASE.indexOf(letter);
			if(originalPosition == -1) { // если пробелы или другие символы вне алфавита
				decryptedMessage.append(letter);
				continue;
			}
			int newPosition = originalPosition + alphabetRightOffset;
			if(newPosition > GlobalVariables.CYRILLIC_ALPHABET_SIZE - 1)
				newPosition -= GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			decryptedMessage.append(isLowerCase ? CYRILLIC_ALPHABET_LOWERCASE.get(newPosition) :
												  CYRILLIC_ALPHABET_UPPERCASE.get(newPosition));
		}
		return decryptedMessage.toString();
	}
	@Override
	public String decrypt(String message) {
		StringBuilder encryptedMessage = new StringBuilder();
		int alphabetLeftOffset = this.key; // переменная объявляется для повышения читаемости
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			boolean isLowerCase = Character.isLowerCase(letter);
			int originalPosition = isLowerCase ? CYRILLIC_ALPHABET_LOWERCASE.indexOf(letter) :
												 CYRILLIC_ALPHABET_UPPERCASE.indexOf(letter);
			if(originalPosition == -1) { // если пробелы или другие символы вне алфавита
				encryptedMessage.append(letter);
				continue;
			}
			int newPosition = originalPosition - alphabetLeftOffset;
			if (newPosition < 0)
				newPosition = GlobalVariables.CYRILLIC_ALPHABET_SIZE - Math.abs(newPosition);
			encryptedMessage.append(isLowerCase ? CYRILLIC_ALPHABET_LOWERCASE.get(newPosition) :
												  CYRILLIC_ALPHABET_UPPERCASE.get(newPosition));
		}
		return encryptedMessage.toString();
	}
	public void printUppercaseCyrillicAlphabet() {
		System.out.print("|");
		CYRILLIC_ALPHABET_UPPERCASE.forEach((letter) -> System.out.print(letter + "|"));
		System.out.println();
	}
	public void printLowercaseCyrillicAlphabet() {
		System.out.print("|");
		CYRILLIC_ALPHABET_LOWERCASE.forEach((letter) -> System.out.print(letter + "|"));
		System.out.println();
	}
	public void printUppercaseEncryptionCyrillicAlphabet() {
		System.out.print("|");
		for (int i = 0; i < CYRILLIC_ALPHABET_UPPERCASE.size(); i++) {
			int index = i + this.key;
			if(index > GlobalVariables.CYRILLIC_ALPHABET_SIZE - 1)
				index -= GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			System.out.print(CYRILLIC_ALPHABET_UPPERCASE.get(index) + "|");
		}
		System.out.println();
	}
	public void printLowercaseEncryptionCyrillicAlphabet() {
		System.out.print("|");
		for (int i = 0; i < CYRILLIC_ALPHABET_LOWERCASE.size(); i++) {
			int index = i + this.key;
			if(index > GlobalVariables.CYRILLIC_ALPHABET_SIZE - 1)
				index -= GlobalVariables.CYRILLIC_ALPHABET_SIZE;
			System.out.print(CYRILLIC_ALPHABET_LOWERCASE.get(index) + "|");
		}
		System.out.println();
	}
	public void printUppercaseDecryptionCyrillicAlphabet() {
		System.out.print("|");
		for (int i = 0; i < CYRILLIC_ALPHABET_UPPERCASE.size(); i++) {
			int index = i - this.key;
			if(index < 0)
				index = CYRILLIC_ALPHABET_UPPERCASE.size() - Math.abs(index);
			System.out.print(CYRILLIC_ALPHABET_UPPERCASE.get(index) + "|");
		}
		System.out.println();
	}
	public void printLowercaseDecryptionCyrillicAlphabet() {
		System.out.print("|");
		for (int i = 0; i < CYRILLIC_ALPHABET_LOWERCASE.size(); i++) {
			int index = i - this.key;
			if(index < 0)
				index = CYRILLIC_ALPHABET_LOWERCASE.size() - Math.abs(index);
			System.out.print(CYRILLIC_ALPHABET_LOWERCASE.get(index) + "|");
		}
		System.out.println();
	}
}
