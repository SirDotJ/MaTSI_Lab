package org.example.lab5;

import java.util.ArrayList;
import java.util.List;

public class BinaryWord {
	private static final int LETTER_BINARY_LENGTH = 8;
	List<Boolean> bitRow;

	BinaryWord(String text) {
		this.bitRow = new ArrayList<>();
		char[] letters = text.toCharArray();
		for (char letter : letters) {
			boolean[] binaryLetter = charToBinary(letter, LETTER_BINARY_LENGTH);
			this.append(binaryLetter);
		}
	}
	BinaryWord(boolean[] values) {
		this.bitRow = new ArrayList<>();
		this.append(values);
	}
	public int getSize() {
		return this.bitRow.size();
	}
	public boolean get(int index) {
		return this.bitRow.get(index);
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
		int maxSize = Math.max(this.getSize(), word.getSize());
		for (int i = 0; i < maxSize; i++) {
			sum += this.get(i) && word.get(i) ? 1 : 0;
		}
		return sum;
	}

	public static void printBinaryWord(BinaryWord word) {
		int size = word.getSize();
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
