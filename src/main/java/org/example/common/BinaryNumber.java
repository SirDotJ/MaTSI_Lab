package org.example.common;

import java.util.BitSet;
import java.util.List;

/* Бинарное представление предоставленного числа */
public class BinaryNumber {
	private static final int DEFAULT_BIT_LENGTH = 8;
	private int bitLength; // Количество бит в двоичном представлении
	private BitSet data;
	public BinaryNumber(int number, int bitLength) {
		this.bitLength = bitLength;
		this.data = BitMath.convertNumberToBitset(number, this.bitLength);
	}
	public BinaryNumber(int number) {
		this(number, DEFAULT_BIT_LENGTH);
	}
	/* Отсчёт начинается с самого старшего бита */
	public byte get(int index) {
		return this.data.get(index) ? (byte) 1 : (byte) 0;
	}

	public long bitwiseAndMultiply(List<Integer> numbers) throws IllegalArgumentException {
		if (numbers.size() != this.getBitLength())
			throw new IllegalArgumentException("Provided numbers list is not of equal size to binary number");

		long sum = 0;
		for (int i = 0; i < numbers.size(); i++) {
			byte bitValue = this.get(i);
			if (bitValue == 1)
				sum += numbers.get(i);
		}

		return sum;
	}

	public int getBitLength() {
		return this.bitLength;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.getBitLength(); i++) {
			builder.append(this.get(i));
		}
		return builder.toString();
	}
}
