package org.togu.lab3;

import org.togu.common.Alphabet;
import org.togu.common.AlphabetConstants;
import org.togu.common.Decryptor;
import org.togu.common.Encryptor;

public class KnapsackEncryptor implements Encryptor, Decryptor {
    private static final Alphabet DEFAULT_ALPHABET = AlphabetConstants.FULL_WITH_SPACE;
    private static final int[] DEFAULT_KEY = new int[] {1, 3, 5, 13, 40, 89};
    public static final int BINARY_REPRESENTATION_LENGTH = 6;

	private final Alphabet alphabet;
    private int[] key;

    public KnapsackEncryptor(Alphabet alphabet, int[] key) throws IllegalArgumentException {
        if (alphabet.size() > Math.pow(2, BINARY_REPRESENTATION_LENGTH))
            throw new IllegalArgumentException("Passed alphabet is too big to fit with binary representation");
        this.alphabet = alphabet;
        this.setKey(key);
    }
    public KnapsackEncryptor(Alphabet alphabet) {
        this(alphabet, DEFAULT_KEY);
    }
    public KnapsackEncryptor(int[] key) {
        this(DEFAULT_ALPHABET, key);
    }
    public KnapsackEncryptor(String key) {
        this(DEFAULT_ALPHABET, parseKey(key));
	}
    public KnapsackEncryptor() {
        this(DEFAULT_ALPHABET, DEFAULT_KEY);
    }

    public void setKey(String key) {
        this.setKey(parseKey(key));
    }
    public void setKey(int[] key) throws IllegalArgumentException {
        if (key.length != BINARY_REPRESENTATION_LENGTH)
            throw new IllegalArgumentException("Passed key does not fit with binary representation");
        this.key = key;
    }

    private static int[] parseKey(String key) {
        String[] numbers = key.split(" ");
        int[] readValues = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            String number = numbers[i];
            readValues[i] = Integer.parseInt(number);
        }
        return readValues;
    }
    public String turnToBinary(char letter) {
        int letterNumber = this.alphabet.indexOf(letter) + 1;
        return turnToBinary(letterNumber);
    }
    public String turnToBinary(int number) {
        String fullBinary = Integer.toBinaryString(number);
        StringBuilder output = new StringBuilder();
        if (fullBinary.length() < BINARY_REPRESENTATION_LENGTH) {
            output.append("0".repeat((BINARY_REPRESENTATION_LENGTH - fullBinary.length())));
        }
        output.append(fullBinary);
        return output.toString();
    }
    private int calculateSumFromLetter(char letter) {
        String binaryRepresentation = turnToBinary(letter);
        int runningSum = 0;
        for (int i = 0; i < binaryRepresentation.length(); i++) {
            int currentNumber = Character.getNumericValue(binaryRepresentation.charAt(i));
            if (currentNumber == 0)
                continue;
            runningSum += this.key[i];
        }
        return runningSum;
    }

    @Override
    public String encrypt (String message) {
        StringBuilder encryptedMessage = new StringBuilder();
        int[] sums = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            char messageLetter = message.charAt(i);
            int letterSum = this.calculateSumFromLetter(messageLetter);
            sums[i] = letterSum;
        }

        encryptedMessage.append(sums[0]);
        for (int i = 1; i < sums.length; i++) {
            encryptedMessage.append(" ").append(sums[i]);
        }

        return encryptedMessage.toString();
    }

    // Не требуется по постановке лабораторной работы
    @Override
    public String decrypt (String message) {
        return "Расшифровки нет";
    }
}
