package org.example.lab3;

import org.example.common.Encryptor;
import org.example.common.GlobalVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KnapsackEncryptor implements Encryptor {

	private static int lengthAlphabet = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.size();
	private static int[] key;

    public static void main(String[] args) {
        KnapsackEncryptor encryptor = new KnapsackEncryptor("1 3 5 7 11");
        System.out.println(encryptor.encrypt("ПРИКАЗ"));
    }
    public KnapsackEncryptor(String key) {
        String[] numbers = key.split(" ");
        this.key = new int[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
            String number = numbers[i];
			this.key[i] = Integer.parseInt(number);
		}
	}

    public String turnToBinary(char letter) {
        int letterNumber = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(letter)) + 1;
        return turnToBinary(letterNumber);
    }

    public String turnToBinary(int number) {
        String fullBinary = Integer.toBinaryString(number);
        StringBuilder output = new StringBuilder();
        if (fullBinary.length() < 5) {
            for (int i = 0; i < (5 - fullBinary.length()); i++) {
                output.append("0");
            }
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

    private int calculateSymbolFromSum(int number) {
        String binaryRepresentation = this.turnToBinary(number);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < binaryRepresentation.length(); i++) {
            int currentNumber = Character.getNumericValue(binaryRepresentation.charAt(i));
            builder.append(currentNumber);
        }
        return -1;
    }

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
            encryptedMessage.append(" " + sums[i]);
        }

        return encryptedMessage.toString();
    }

    // in progress
    public String decrypt (String text) {
        return "-1";
    }
}
