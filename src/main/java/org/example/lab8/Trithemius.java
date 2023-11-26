package org.example.lab8;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Trithemius {
//    public static List<Integer> input = new ArrayList<>(Arrays.asList(
//
//    ))

	public static final Function<Integer, Integer> exampleValidTrithemiusFunction = integer -> -51 * (int) Math.pow(integer, 2) - 93 * integer - 66;

	public static final String message = "тритемиус";
	public static void main(String[] args) {
//		Alphabet usedAlphabet = AlphabetConstants.CYRILLIC_WITH_SPACE;
//		System.out.println("UsedAlphabet:");
//		for (int i = 0; i < usedAlphabet.size(); i++) {
//			System.out.print(usedAlphabet.get(i));
//		}
//		System.out.println();
//
//		int functionPower = 100;
//		Function<Integer, Integer> validTrithemiusFunction = generateRandomValidTrithemiusFunction(usedAlphabet, functionPower);
////		Alphabet cypherAlphabet = generateCypherAlphabet(usedAlphabet, validTrithemiusFunction);
//		Alphabet cypherAlphabet = generateCypherAlphabet(usedAlphabet, exampleValidTrithemiusFunction);
//		System.out.println("CypherAlphabet:");
//		for (int i = 0; i < cypherAlphabet.size(); i++) {
//			System.out.print(cypherAlphabet.get(i));
//		}
//		System.out.println();
//
//		for (int i = 0; i < usedAlphabet.size(); i++) {
//			int newPosition = (i + exampleValidTrithemiusFunction.apply(i)) % usedAlphabet.size();
//			if (newPosition < 0)
//				newPosition = usedAlphabet.size() - Math.abs(newPosition);
//			System.out.println(newPosition);
//		}

		Alphabet usedAlphabet = AlphabetConstants.CYRILLIC_WITH_SPACE;
		Alphabet cypherAlphabet = generateCypherAlphabet(usedAlphabet, exampleValidTrithemiusFunction);
		System.out.println("Message: " + message);
		String encryptedMessage = encrypt(cypherAlphabet, usedAlphabet, message);
		System.out.println("Encrypted message: " + encryptedMessage);
		String decryptedMessage = decrypt(cypherAlphabet, usedAlphabet, encryptedMessage);
		System.out.println("Decrypted message: " + decryptedMessage);
	}

	public static String encrypt(Alphabet encryptionAlphabet, Alphabet sourceAlphabet, String message) {
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			int oldPosition = sourceAlphabet.indexOf(letter);
			char newLetter = encryptionAlphabet.get(oldPosition);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}

	public static String decrypt(Alphabet encryptionAlphabet, Alphabet usedAlphabet, String encryptedMessage) {
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < encryptedMessage.length(); i++) {
			char letter = encryptedMessage.charAt(i);
			int oldPosition = encryptionAlphabet.indexOf(letter);
			char newLetter = usedAlphabet.get(oldPosition);
			decryptedMessage.append(newLetter);
		}
		return decryptedMessage.toString();
	}

	public static Alphabet generateCypherAlphabet(Alphabet alphabet, Function<Integer, Integer> function) {
		List<Integer> shifts = generateCypher(alphabet, function);
		List<Character> newAlphabetCharacters = new ArrayList<>();
		int N = alphabet.size();
		for (int i = 0; i < N; i++) {
			int newPosition = (i + shifts.get(i)) % N;
			if (newPosition < 0)
				newPosition = N - Math.abs(newPosition);
			newAlphabetCharacters.add(alphabet.get(newPosition));
		}
		return new Alphabet(newAlphabetCharacters, alphabet.type());
	}

	public static List<Integer> generateCypher(Alphabet alphabet, Function<Integer, Integer> function) {
		List<Integer> shifts = new ArrayList<>();
		for (int i = 0; i < alphabet.size(); i++)
			shifts.add(function.apply(i));
		return shifts;
	}

	public static Function<Integer, Integer> generateRandomValidTrithemiusFunction(Alphabet alphabet, int power) {
		Function<Integer, Integer> function;
		boolean success = false;
		do {
			int coefficient1 = (int) (Math.random() * power - power);
			int coefficient2 = (int) (Math.random() * power - power);
			int coefficient3 = (int) (Math.random() * power - power);
			function = cypherFunctionGenerator(coefficient1, coefficient2, coefficient3);
			try {
				Alphabet cypherAlphabet = generateCypherAlphabet(alphabet, function);
			} catch (IllegalArgumentException e) {
				continue;
			}
			success = true;
		} while (!success);
		return function;
	}

	public static Function<Integer, Integer> cypherFunctionGenerator(int coefficient1, int coefficient2, int coefficient3) {
		return integer -> coefficient1 * (int) Math.pow(integer, 2) + coefficient2 * integer + coefficient3;
	}
}
