package org.example.lab4;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;
import org.example.common.Decryptor;
import org.example.common.Encryptor;

public class Playfair implements Encryptor, Decryptor {
	final static private Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_NO_SPACE;

	private final Alphabet alphabet;
	public Playfair(Alphabet alphabet) {
		this.alphabet = alphabet;
	}
	public Playfair() {
		this(DEFAULT_ALPHABET);
	}
	@Override
	public String encrypt(String message) {
		return null;
	}

	@Override
	public String decrypt(String message) {
		return null;
	}
}
