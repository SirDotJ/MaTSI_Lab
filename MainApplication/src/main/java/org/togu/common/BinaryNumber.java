package org.togu.common;

import java.util.BitSet;
import java.util.List;

/* Бинарное представление предоставленного числа */
public class BinaryNumber {
	private static final int DEFAULT_BIT_LENGTH = 8;
	private int bitLength; // Количество бит в двоичном представлении
	private BitSet data;
	public BinaryNumber(long number, int bitLength) {
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

	public BinaryNumber bitwiseXOR(BinaryNumber other) throws IllegalArgumentException {
		if (this.getBitLength() != other.getBitLength())
			throw new IllegalArgumentException("Provided binary number is not of same length for xor");
		long xorSum = 0;
		long mask = (long) Math.pow(2, this.getBitLength());
		for (int i = 0; i < this.getBitLength(); i++) {
			int xor = this.get(i) ^ other.get(i);
			mask >>= 1;
			xorSum += mask * xor;
		}
		return new BinaryNumber(xorSum, this.getBitLength());
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

	public static void main(String[] args) {
		BinaryNumber number1 = new BinaryNumber(124, 32);
		BinaryNumber number2 = new BinaryNumber(241, 32);
		System.out.println(number1);
		System.out.println(number2);
		System.out.println(number1.bitwiseXOR(number2));
	}
}
