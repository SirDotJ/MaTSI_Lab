package org.togu.lab8;

import org.togu.common.Alphabet;
import org.togu.common.AlphabetConstants;
import org.togu.common.Decryptor;
import org.togu.common.Encryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Trithemius implements Encryptor, Decryptor {
	/* Из примера */
	private final static Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	private final static Function<Integer, Integer> DEFAULT_SHIFT_FUNCTION = integer -> -825 * (int) Math.pow(integer, 2) - 315 * integer - 601;

	private Alphabet alphabet;
	private Alphabet cypherAlphabet;

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

	public void setNewKey(Function<Integer, Integer> shiftFunction) throws IllegalArgumentException {
		try {
			this.cypherAlphabet = calculateCypherAlphabet(this.alphabet, shiftFunction);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Provided shiftFunction does not allow for a valid cypher alphabet to be made!");
		}
	}

	public void setNewAlphabet(Alphabet alphabet) {
		this.alphabet = alphabet;
	}

	public Alphabet getAlphabet() {
		return alphabet;
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
