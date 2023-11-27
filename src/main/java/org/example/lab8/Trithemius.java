package org.example.lab8;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;
import org.example.common.Decryptor;
import org.example.common.Encryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Trithemius implements Encryptor, Decryptor {
	/* Из примера */
	private final static Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	private final static Function<Integer, Integer> DEFAULT_SHIFT_FUNCTION = integer -> -51 * (int) Math.pow(integer, 2) - 93 * integer - 66;

	private final Alphabet alphabet;
	private final Alphabet cypherAlphabet;

	public static void main(String[] args) {
		Trithemius encryptor = new Trithemius();
		String message = "когда_сюда_подойдут_варвары";
		String encryptedMessage = encryptor.encrypt(message);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);

		System.out.println("Message: " + message);
		System.out.println("Encrypted message: " + encryptedMessage);
		System.out.println("Decrypted message: " + decryptedMessage);
	}

	public Trithemius(Alphabet alphabet, Function<Integer, Integer> shiftFunction) throws IllegalArgumentException {
		this.alphabet = alphabet;
		try {
			this.cypherAlphabet = calculateCypherAlphabet(this.alphabet, shiftFunction);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Provided shiftFunction does not allow for a valid cypher alphabet to be made!");
		}
	}

	public Trithemius(Alphabet alphabet) {
		this(alphabet, DEFAULT_SHIFT_FUNCTION);
	}

	public Trithemius(Function<Integer, Integer> shiftFunction) {
		this(DEFAULT_ALPHABET, shiftFunction);
	}

	public Trithemius() {
		this(DEFAULT_ALPHABET, DEFAULT_SHIFT_FUNCTION);
	}

	public static Alphabet calculateCypherAlphabet(Alphabet alphabet, Function<Integer, Integer> function) throws IllegalArgumentException {
		List<Integer> shifts = generateCypher(alphabet, function);
		List<Character> newAlphabetCharacters = new ArrayList<>();
		int N = alphabet.size();
		for (int i = 0; i < N; i++) {
			int newPosition = (i + shifts.get(i)) % N;
			if (newPosition < 0)
				newPosition = N - Math.abs(newPosition);
			newAlphabetCharacters.add(alphabet.get(newPosition));
		}
		Alphabet cypherAlphabet;
		try {
			cypherAlphabet = new Alphabet(newAlphabetCharacters, alphabet.type());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Provided function does not allow for a valid cypher to be made");
		}
		return cypherAlphabet;
	}

	public static List<Integer> generateCypher(Alphabet alphabet, Function<Integer, Integer> function) {
		List<Integer> shifts = new ArrayList<>();
		for (int i = 0; i < alphabet.size(); i++)
			shifts.add(function.apply(i));
		return shifts;
	}

	@Override
	public String encrypt(String message) {
		StringBuilder encryptedMessage = new StringBuilder();
		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			int oldPosition = this.alphabet.indexOf(letter);
			char newLetter = this.cypherAlphabet.get(oldPosition);
			encryptedMessage.append(newLetter);
		}
		return encryptedMessage.toString();
	}

	@Override
	public String decrypt(String encryptedMessage) {
		StringBuilder decryptedMessage = new StringBuilder();
		for (int i = 0; i < encryptedMessage.length(); i++) {
			char letter = encryptedMessage.charAt(i);
			int oldPosition = this.cypherAlphabet.indexOf(letter);
			char newLetter = this.alphabet.get(oldPosition);
			decryptedMessage.append(newLetter);
		}
		return decryptedMessage.toString();
	}
}
