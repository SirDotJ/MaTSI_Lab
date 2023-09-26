package org.example.lab2;

import org.example.common.Encryptor;

import java.util.ArrayList;
import java.util.Arrays;

// Для реализации была взята первая формула mod N(k_i - X_i)
public class BofortEncryptor implements Encryptor {
	final static private String TEST_KEY = "КЛЮЧ";
	final static public ArrayList<Character> CYRILLIC_ALPHABET_LOWERCASE = new ArrayList<>(Arrays.asList(
			'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '_'
	));
	final static public ArrayList<Character> CYRILLIC_ALPHABET_UPPERCASE = new ArrayList<>(Arrays.asList(
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', '_'
	));
	private ArrayList<Character> encryptedCyrillicAlphabetUppercase;
	private ArrayList<Character> encryptedCyrillicAlphabetLowercase;
	public static void main(String[] args) {
		BofortEncryptor encryptor = new BofortEncryptor(TEST_KEY);
		String message = "СООБЩЕНИЕ_ПОЛЬЗОВАТЕЛЮ";
		String encryptedMessage = encryptor.encrypt(message);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);
		System.out.println("Сообщение: " + message);
		System.out.println("Зашифрованное сообщение: " + encryptedMessage);
		System.out.println("Расшифрованное сообщение: " + decryptedMessage);
//		System.out.println("Обычный алфавит:");
//		System.out.println(encryptor.getOriginalAlphabetUppercase());
//		System.out.println("Зашифрованный алфавит:");
//		System.out.println(encryptor.getEncryptedAlphabetUppercase());
	}
	private String key;
	public BofortEncryptor(String key) {
		this.key = key;
	}
	@Override
	public String encrypt(String message) {
		StringBuilder encryptedMessage = new StringBuilder();
		int messageLength = message.length();
		int keyLength = key.length();
		ArrayList<Integer> messageKeyNumbers = new ArrayList<>();
		if (messageLength > keyLength) {
			int count = 0;
			while (keyLength < messageLength) {
				key += key.charAt(count);
				keyLength++;
				count++;
			}
		}

		for (int i = 0; i < messageLength; i++) {
			int numberMessage = 0;
			int numberKey = 0;
			for (int j = 0; j < CYRILLIC_ALPHABET_UPPERCASE.size(); j++) {
				if (CYRILLIC_ALPHABET_UPPERCASE.get(j) == message.charAt(i)) {
					numberMessage = j + 1;
				}
				if (CYRILLIC_ALPHABET_UPPERCASE.get(j) == key.charAt(i)) {
					numberKey = j + 1;
				}
				if (numberMessage != 0 && numberKey != 0) {
					if (numberMessage < numberKey) {
                        numberMessage += CYRILLIC_ALPHABET_UPPERCASE.size();
						messageKeyNumbers.add((numberMessage - numberKey) % CYRILLIC_ALPHABET_UPPERCASE.size());
                    }
                    else if (numberMessage == numberKey) {
						messageKeyNumbers.add(CYRILLIC_ALPHABET_UPPERCASE.size());
                    }
                    else {
						messageKeyNumbers.add((numberMessage - numberKey) % CYRILLIC_ALPHABET_UPPERCASE.size());
                    }

					break;
				}
			}
		}

		for (int i = 0; i < messageLength; i++ ) {
            for (int j = 0; j < CYRILLIC_ALPHABET_UPPERCASE.size(); j++) {
                if(messageKeyNumbers.get(i)-1 == j) {
                    encryptedMessage.append(CYRILLIC_ALPHABET_UPPERCASE.get(j));
                }
            }
        }

		return encryptedMessage.toString();
	}

	@Override
	public String decrypt(String message) {
		StringBuilder decryptedMessage = new StringBuilder();
        int lengthMessage = message.length();
        int lengthKey = this.key.length();
        int[] messageKeyNumber = new int [lengthMessage];
        if (lengthMessage > lengthKey) {
            int count = 0;
            while( lengthKey < lengthMessage){
                key += key.charAt(count);
                lengthKey++;
                count++;
            }
        }

        for (int i = 0; i < lengthMessage; i++ ) {
            int numberMessage = 0;
            int numberKey = 0;
            for(int j = 0; j < CYRILLIC_ALPHABET_UPPERCASE.size(); j++) {
                if (CYRILLIC_ALPHABET_UPPERCASE.get(j) == message.charAt(i)) {
                    numberMessage = j+1;
                }
                if (CYRILLIC_ALPHABET_UPPERCASE.get(j) == key.charAt(i)) {
                    numberKey = j+1;
                }
                if (numberMessage != 0 && numberKey != 0 ) {
                    messageKeyNumber[i] = (numberMessage + numberKey) % CYRILLIC_ALPHABET_UPPERCASE.size();
                    break;
                }
            }

        }

        for (int i = 0; i < lengthMessage; i++ ) {
            for (int j = 0; j < CYRILLIC_ALPHABET_UPPERCASE.size(); j++) {
                if(messageKeyNumber[i]-1 == j) {
                    decryptedMessage.append(CYRILLIC_ALPHABET_UPPERCASE.get(j));
                }
            }
        }

        return decryptedMessage.toString();
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
//	public String getEncryptedAlphabetUppercase() {
//		StringBuilder output = new StringBuilder();
//		for (int i = 0; i < CYRILLIC_ALPHABET_UPPERCASE.size(); i++) {
//			int oldIndex = i;
//			int newIndex = (oldIndex - this.key.charAt(i)) % CYRILLIC_ALPHABET_UPPERCASE.size();
//			if (newIndex < 0)
//				newIndex = CYRILLIC_ALPHABET_UPPERCASE.size() - Math.abs(newIndex);
//			output.append(CYRILLIC_ALPHABET_UPPERCASE.get(newIndex));
//		}
//		return output.toString();
//	}
//	public String getEncryptedAlphabetLowercase() {
//		StringBuilder output = new StringBuilder();
//		for (int i = 0; i < CYRILLIC_ALPHABET_LOWERCASE.size(); i++) {
//			int oldIndex = i;
//			int newIndex = (oldIndex - this.key.get(i)) % CYRILLIC_ALPHABET_LOWERCASE.size();
//			if (newIndex < 0)
//				newIndex = CYRILLIC_ALPHABET_LOWERCASE.size() - Math.abs(newIndex);
//			output.append(CYRILLIC_ALPHABET_LOWERCASE.get(newIndex));
//		}
//		return output.toString();
//	}
}
