package org.example.lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HemmingCode {
	public static void main(String[] args) {
		boolean makeAnError = true;
		int errorPosition = 20;

		String message = "bl";
		System.out.println("Введено сообщение: " + message);

		HemmingCode hemmingCode = new HemmingCode();
		BinaryWord messageToTranser = hemmingCode.secure(message);
		System.out.println("Защищенное представление сообщения:\n" + messageToTranser);

		// Вставим ошибку
		if (makeAnError) {
			messageToTranser.set(errorPosition, !messageToTranser.get(errorPosition));
		}

		System.out.println("Получено сообщение: ");
		System.out.println(messageToTranser);
		if (makeAnError)
			System.out.println("Была совершена ошибка на позиции: " + errorPosition);

		int checkResults = hemmingCode.check(messageToTranser);
		System.out.println("Результат проверки на ошибку: " + checkResults);
		if (checkResults == -1) {
			System.out.println("Сообщение не было изменено");
			if (makeAnError) {
				System.out.println("!!!Некорректный результат");
			} else {
				System.out.println("Результат корректный");
			}
		} else if (checkResults == errorPosition) {
			System.out.println("Правильно определена ошибка на позиции " + checkResults);
		} else {
			System.out.println("!!!Ошибка некорректно определена на позиции " + checkResults);
		}
	}
	private static void printBooleanArrayAsBinary(boolean[] values) {
		for (boolean value :
				values) {
			System.out.print(value ? '1' : '0');
		}
	}
	private static void printHemmingWords(List<BinaryWord> words) {
		for (BinaryWord word :
				words) {
			BinaryWord.printBinaryWord(word);
			System.out.println();
		}
	}
	public static void insertHemmingBits(BinaryWord target) {
		int size = target.getSize();
		int highestPowerTwo = findNearestHigherPowerOfTwo(size);
		for (int power = 1; power < (1 << highestPowerTwo); power <<= 1) {
			target.insert(power-1, false);
		}
	}

	public static BinaryWord getMessageWithNoSum(BinaryWord word) {
		BinaryWord cleanWord = new BinaryWord(word.getValues());

		int size = cleanWord.getSize();
		int highestPowerTwo = findNearestHigherPowerOfTwo(size);
		for (int hemmingIndex = 1; hemmingIndex < (1 << highestPowerTwo); hemmingIndex <<= 1) {
			cleanWord.set(hemmingIndex - 1, false);
		}

		return cleanWord;
	}

	public static int findNearestHigherPowerOfTwo(int number) {
		int value = 1;
		int power = 0;
		while (value <= number) {
			value <<= 1;
			power++;
		}
		return power;
	}
	public static List<BinaryWord> getHemmingWords(BinaryWord word) {
		int size = word.getSize();
		int highestPowerTwo = findNearestHigherPowerOfTwo(size);
		List<BinaryWord> hammingWords = new ArrayList<>();
		for (int power = 1; power < (1 << highestPowerTwo); power <<= 1) {
			hammingWords.add(getWordForHemmingNumber(word, power));
		}
		return hammingWords;
	}

	public static BinaryWord getWordForHemmingNumber(BinaryWord word, int number) {
		boolean[] values = new boolean[word.getSize()];
		Arrays.fill(values, false);

		for (int i = number - 1; i < values.length; i += number * 2) {
			for (int j = 0; j < Math.min(number, values.length - i); j++) {
				values[i + j] = true;
			}
		}

		return new BinaryWord(values);
	}

    public BinaryWord secure(String message) {
		BinaryWord word = new BinaryWord(message);
		insertHemmingBits(word);
		List<BinaryWord> hemmingMasks = getHemmingWords(word);

		int indexToSet = 1;
		for (BinaryWord mask : hemmingMasks) {
			int foundSum = word.andSum(mask);
			word.set(indexToSet - 1, foundSum % 2 != 0);
			indexToSet <<= 1;
		}
		return word;
    }

	/* Возвращает индекс найденной ошибки, -1 если ошибок нет */
    public int check(BinaryWord receivedMessage) {
		BinaryWord checkCopy = getMessageWithNoSum(receivedMessage);

		List<BinaryWord> hemmingMasks = getHemmingWords(checkCopy);
		int hemmingCodeIndex = 1;
		int errorSum = 0;
		for (BinaryWord mask : hemmingMasks) {
			int foundSum = checkCopy.andSum(mask);
			boolean sign = foundSum % 2 != 0;
			if (sign != receivedMessage.get(hemmingCodeIndex - 1))
				errorSum += hemmingCodeIndex;
			hemmingCodeIndex <<= 1;
		}

		if (errorSum == 0)
        	return -1;
		else
			return errorSum - 1;
    }
}
