package org.example.lab3;

import org.example.common.Encryptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnapsackEncryptor implements Encryptor {
	final static public ArrayList<Character> CYRILLIC_ALPHABET_UPPERCASE = new ArrayList<>(Arrays.asList(
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', '_'
	));
	private static int lengthAlphabet = CYRILLIC_ALPHABET_UPPERCASE.size();
	private static ArrayList<Integer> key;
	public KnapsackEncryptor(String key) {
		this.key = new ArrayList<>();
        String[] numbers = key.split(" ");
		for (int i = 0; i < numbers.length; i++) {
			this.key.add(Integer.parseInt(numbers[i]));
		}
	}

	@Override
	public String encrypt(String message) {
		// получение длины сообщения
        int textLength = message.length();
        String[] binary = new String[textLength];
        int[] resultNumbers = new int[textLength];
        String resultEncryption = new String();

        for (int i = 0; i < textLength; i++) {
            for (int j = 0; j < lengthAlphabet; j++) {
                if(message.charAt(i) == CYRILLIC_ALPHABET_UPPERCASE.get(j)) {
                    binary[i] = Integer.toBinaryString(j + 1);
                    break;
                }
            }
        }

        for (int i = 0; i < textLength; i++) {
            if (binary[i].length() < 5) {
                String temp = new String();
                for(int j = 0; j < 5 - binary[i].length(); j++) {
                    temp += '0';
                }
                binary[i] = temp + binary[i];
            }
        }

        for (int i = 0; i < textLength; i++) {
            for(int j = 0; j < binary[i].length(); j++) {
                if (binary[i].charAt(j) == '1') {
                    resultNumbers[i] += key.get(j);
                }
            }
        }


        for (int i = 0; i < textLength; i++) {
            resultEncryption += resultNumbers[i] + " ";
        }
        return resultEncryption;
	}

	@Override
	public String decrypt(String message) {
		String[] strArray = message.split(" ");
        int[] textNumber = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            textNumber[i] = Integer.parseInt(strArray[i]);
        }
        int[] resultNumber = new int[strArray.length];
        String[] binaryResult = new String[strArray.length];
        String resultDecryption = new String();
        for (int i = 0; i < strArray.length; i++) {
            binaryResult[i] = new String();
        }
        for (int i = 0; i < strArray.length; i++) {
            List<Integer> result = searchSumm(key, textNumber[i]);
            int count = 0;

            for (int k = 0; k < 5; k++) {
                if(count != result.size()) {
                    if (result.get(count) == key.get(k)) {
                        binaryResult[i] += '1';
                        count++;
                    } else {
                        binaryResult[i] += '0';
                    }
                }
                else
                    binaryResult[i] += '0';
            }

        }
        for(int i = 0; i < strArray.length; i++) {
            resultDecryption += CYRILLIC_ALPHABET_UPPERCASE.get(Integer.parseInt(binaryResult[i], 2) - 1);
        }
        return resultDecryption;
	}

    private static List<Integer> searchSumm(ArrayList<Integer> key, int tempSymbol) {
        List<Integer> number = new ArrayList<>();
        boolean[] visited = new boolean[key.size()];
        if (searchSumm(key, tempSymbol, 0, number, visited)) {
            return number;
        } else {
            return null; // Если набор не найден
        }
    }

    private static boolean searchSumm(ArrayList<Integer> key, int tempSymbol, int tempIndex, List<Integer> number, boolean[] visited) {
        if (tempSymbol == 0) {
            return true; // Набор найден
        }

        if (tempIndex >= key.size() || tempSymbol < 0) {
            return false; // Набор невозможен
        }

        if (!visited[tempIndex]) {
            number.add(key.get(tempIndex));
            visited[tempIndex] = true;

            if (searchSumm(key, tempSymbol - key.get(tempIndex), tempIndex + 1, number, visited)) {
                return true; // Набор найден
            }

            number.remove(number.size() - 1);
            visited[tempIndex] = false;
        }

        return searchSumm(key, tempSymbol, tempIndex + 1, number, visited);
    }
}
