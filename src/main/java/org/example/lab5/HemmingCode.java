package org.example.lab5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HemmingCode {
	private static final int DEFAULT_SUBDIVISION_COUNT = 2;
	private HemmingSecuredMessage securedMessage;

	public static void main(String[] args) {
		String message = "block";
		int subdivisionCount = 1;
		System.out.println("Message to transfer: " + message);
		System.out.println("Subdivision count: " + subdivisionCount);
		HemmingCode messageToTransfer = new HemmingCode(message, subdivisionCount);
		System.out.println("Generated message:\n" + messageToTransfer);
		int[] changedIndexes = messageToTransfer.corruptMessage();
		System.out.println("Corrupted message:\n" + messageToTransfer);
		System.out.println("Corrupted indexes:\n" + Arrays.toString(changedIndexes));
		messageToTransfer.fix();
		System.out.println("Fixed message:\n" + messageToTransfer);
	}

	HemmingCode(String message, int subdivisionCount) {
		this.securedMessage = new HemmingSecuredMessage(message, subdivisionCount);
	}
	HemmingCode(String message) {
		this(message, DEFAULT_SUBDIVISION_COUNT);
	}
	public void secure(String message) {
		this.secure(message, DEFAULT_SUBDIVISION_COUNT);
	}
	public void secure(String message, int subdivisionCount) {
		this.securedMessage = new HemmingSecuredMessage(message, subdivisionCount);
	}
	public boolean check() {
		int[] errorCheckResults = this.securedMessage.check();
		for (int index:
			 errorCheckResults) {
			if (index != -1)
				return false;
		}
		return true;
	}

	public int[] corruptMessage() {
		int[] changedIndexes = new int[this.securedMessage.size()];
		for (int i = 0; i < this.securedMessage.size(); i++) {
			BinaryWord word = this.securedMessage.get(i);
			int max = word.size();
			int min = 0;
			changedIndexes[i] = (int) ((Math.random() * (max - min)) + min);
			word.flip(changedIndexes[i]);
		}
		return changedIndexes;
	}

	public int[] getErrors() {
		return this.securedMessage.check();
	}
	public void fix() {
		if (this.check())
			return;

		int[] errorIndexes = this.getErrors();
		int index = 0;
		for (int errorIndex : errorIndexes)
			this.securedMessage.get(index++).flip(errorIndex);
	}
	public String getMessage() {
		return this.securedMessage.toString();
	}

	public static void insertHemmingBits(BinaryWord target) {
		int size = target.size();
		int highestPowerTwo = findNearestHigherPowerOfTwo(size);
		for (int power = 1; power < (1 << highestPowerTwo); power <<= 1) {
			target.insert(power-1, false);
		}
	}

	public static BinaryWord getMessageWithNoSum(BinaryWord word) {
		BinaryWord cleanWord = new BinaryWord(word.getValues());

		int size = cleanWord.size();
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
		int size = word.size();
		int highestPowerTwo = findNearestHigherPowerOfTwo(size);
		List<BinaryWord> hammingWords = new ArrayList<>();
		for (int power = 1; power < (1 << highestPowerTwo); power <<= 1) {
			hammingWords.add(getWordForHemmingNumber(word, power));
		}
		return hammingWords;
	}

	public static BinaryWord getWordForHemmingNumber(BinaryWord word, int number) {
		boolean[] values = new boolean[word.size()];
		Arrays.fill(values, false);

		for (int i = number - 1; i < values.length; i += number * 2) {
			for (int j = 0; j < Math.min(number, values.length - i); j++) {
				values[i + j] = true;
			}
		}

		return new BinaryWord(values);
	}

	static BinaryWord secureWord(String message) {
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

	static int checkWord(BinaryWord word) {
		BinaryWord checkCopy = getMessageWithNoSum(word);

		List<BinaryWord> hemmingMasks = getHemmingWords(checkCopy);
		int hemmingCodeIndex = 1;
		int errorSum = 0;
		for (BinaryWord mask : hemmingMasks) {
			int foundSum = checkCopy.andSum(mask);
			boolean sign = foundSum % 2 != 0;
			if (sign != word.get(hemmingCodeIndex - 1))
				errorSum += hemmingCodeIndex;
			hemmingCodeIndex <<= 1;
		}

		if (errorSum == 0)
        	return -1;
		else
			return errorSum - 1;
    }

	@Override
	public String toString() {
		return this.securedMessage.toString();
	}
}
