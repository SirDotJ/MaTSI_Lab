package org.example.common;

import java.util.BitSet;

/* Класс для работы с битовым представлением данных */
public class BitMath {
	public static void main(String[] args) {
		int number = 1234;
		int bitCount = 16;
		BitSet bitSet = convertNumberToBitset(number, bitCount);
		for (int i = 0; i < bitCount; i++) {
			System.out.print(bitSet.get(i) ? "1" : "0");
		}
	}
	public static BitSet generateBitset(int numberOfBits) {
		return new BitSet(numberOfBits);
	}
	public static BitSet convertNumberToBitset(long number, int numberOfBits) {
		BitSet bitSet = generateBitset(numberOfBits);
		int highestPower = findHighestClosestPowerOfTwo(number);
		int counter = (int) (numberOfBits - highestPower - 1);
		for (long i = highestPower; i >= 0; i--) {
			long mask = (long) Math.pow(2, i);
			long one = number & mask;
			bitSet.set(counter++, one > 0);
			if (i == 0)
				break;
		}
		return bitSet;
	}
	public static int findHighestClosestPowerOfTwo(long number) {
		int power = 0;
		do {
			power++;
		} while (Math.pow(2, power) < number);
		return power;
	}

}
