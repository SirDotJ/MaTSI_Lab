package org.example.lab5;

import java.util.ArrayList;
import java.util.List;

public class BinaryWord {
	public static final int LETTER_BINARY_LENGTH = 8;
	List<Boolean> bitRow;

	public BinaryWord(String text) {
		this.bitRow = new ArrayList<>();
		char[] letters = text.toCharArray();
		for (char letter : letters) {
			boolean[] binaryLetter = charToBinary(letter, LETTER_BINARY_LENGTH);
			this.append(binaryLetter);
		}
	}
	public BinaryWord(boolean[] values) {
		this.bitRow = new ArrayList<>();
		this.append(values);
	}
	public BinaryWord(char[] letters) {
		this(String.copyValueOf(letters));
	}
	public int size() {
		return this.bitRow.size();
	}
	public boolean get(int index) {
		return this.bitRow.get(index);
	}
	public boolean[] getValues() {
		boolean[] values = new boolean[this.size()];
		for (int i = 0; i < this.size(); i++) {
			values[i] = this.get(i);
		}
		return values;
	}
	public String getCleanWord() {
		StringBuilder cleanWord = new StringBuilder();
		int currentLetterIndex = 7;
		int currentLetterValue = 0;
		int indexToIgnore = 1;
		for (int i = 0; i < this.size(); i++) {
			if (i == indexToIgnore - 1) {
				indexToIgnore = indexToIgnore << 1;
				continue;
			}
			boolean value = this.get(i);
			int currentValue = (int) Math.pow(2, currentLetterIndex--) * (value ? 1 : 0);
			currentLetterValue += currentValue;
			if (currentLetterIndex <= -1) {
				cleanWord.append(Character.toString(currentLetterValue));
				currentLetterValue = 0;
				currentLetterIndex = 7;
			}
		}
		return cleanWord.toString();
	}

	public void set(int index, boolean value) {
		this.bitRow.set(index, value);
	}
	public void flip(int index) {
		boolean value = this.get(index);
		this.set(index, !value);
	}
	public void append(boolean[] values) {
		for (boolean value : values)
			this.append(value);
	}
	public void append(boolean value) {
		this.bitRow.add(value);
	}
	public void insert(int index, boolean value) {
		this.bitRow.add(index, value);
	}

	public int andSum(BinaryWord word) {
		int sum = 0;
		int maxSize = Math.max(this.size(), word.size());
		for (int i = 0; i < maxSize; i++) {
			sum += this.get(i) && word.get(i) ? 1 : 0;
		}
		return sum;
	}

	@Override
	public String toString() {
		StringBuilder representation = new StringBuilder();
		for (int i = 0; i < this.size(); i++) {
			representation.append(this.get(i) ? '1' : '0');
		}
		return representation.toString();
	}

	public static void printBinaryWord(BinaryWord word) {
		int size = word.size();
		for (int i = 0; i < size; i++) {
			boolean value = word.get(i);
			System.out.print(value ? 1 : 0);
		}
	}
	private static boolean[] charToBinary(char letter, int binaryLength) {
		boolean[] binaryLetter = new boolean[binaryLength];
		int index = 0;
		for (int j = 1 << binaryLetter.length - 1; j >= 1; j >>= 1) {
			binaryLetter[index++] = ((byte) letter & j) > 0;
		}
		return binaryLetter;
	}
}
