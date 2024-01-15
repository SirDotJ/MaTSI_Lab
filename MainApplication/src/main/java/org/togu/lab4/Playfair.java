package org.togu.lab4;

import javafx.util.Pair;
import org.togu.common.Alphabet;
import org.togu.common.AlphabetConstants;
import org.togu.common.Decryptor;
import org.togu.common.Encryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Playfair implements Encryptor, Decryptor {
	final static private Alphabet DEFAULT_ALPHABET = AlphabetConstants.LATIN_NO_SPACE;
	final static private String DEFAULT_KEY = "TEST";
	private final AlphabetMatrix matrix;

	public Playfair(String key, Alphabet alphabet) {
		this.matrix = new AlphabetMatrix(key, alphabet);
	}
	public Playfair(Alphabet alphabet) {
		this(DEFAULT_KEY, alphabet);
	}
	public Playfair(String key) {
		this(key, DEFAULT_ALPHABET);
	}
	public Playfair() {
		this(DEFAULT_KEY, DEFAULT_ALPHABET);
	}

	public void setKey(String newKey) {
		this.matrix.initializeMatrix(newKey);
	}
	public void setKey(String newKey, Alphabet newAlphabet) {
		this.matrix.initializeMatrix(newKey, newAlphabet);
	}

	public void printKeyMatrix() {
		for (int i = 0; i < this.matrix.getRowCount(); i++) {
			for (int j = 0; j < this.matrix.getColumnCount(); j++) {
				System.out.print(this.matrix.get(i, j));
			}
			System.out.println();
		}
	}

	private List<Bigramm> splitToBigramms(String input) {
		List<Bigramm> bigramms = new ArrayList<>();

		int counter = 0;
		char lastSymbol = 'x';
		for (int i = 0; i < input.length(); i++) {
			char letter = Character.toLowerCase(input.charAt(i));
			if (counter++ % 2 == 0) {
				lastSymbol = letter;
				continue;
			}
			if (letter == lastSymbol) {
				bigramms.add(new Bigramm(lastSymbol, this.matrix.getType() == Alphabet.TYPE.CYRILLIC ? 'ъ' : 'x'));
				lastSymbol = this.matrix.getType() == Alphabet.TYPE.CYRILLIC ? 'ъ' : 'x';
				i--;
				continue;
			}
			bigramms.add(new Bigramm(lastSymbol, letter));
		}
		if (counter % 2 == 1)
			bigramms.add(new Bigramm(lastSymbol, this.matrix.getType() == Alphabet.TYPE.CYRILLIC ? 'ъ' : 'x'));

		return bigramms;
	}

	private boolean sameRow(Pair<Integer, Integer> coordinate1, Pair<Integer, Integer> coordinate2) {
		return coordinate1.getKey().equals(coordinate2.getKey());
	}
	private boolean sameColumn(Pair<Integer, Integer> coordinate1, Pair<Integer, Integer> coordinate2) {
		return coordinate1.getValue().equals(coordinate2.getValue());
	}

	private String[] biagrammsToString(List<Bigramm> biagramms) {
		String[] result = new String[biagramms.size()];
		for (int i = 0; i < biagramms.size(); i++) {
			StringBuilder biagrammStringBuilder = new StringBuilder();
			biagrammStringBuilder.append(biagramms.get(i).getFirstLetter());
			biagrammStringBuilder.append(biagramms.get(i).getSecondLetter());
			result[i] = biagrammStringBuilder.toString();
		}
		return result;
	}

	@Override
	public String encrypt(String message) {
		message = message.trim();
		List<Bigramm> bigramms = splitToBigramms(message);
		List<List<Bigramm>> encryptedMessageVariantsInBigramms = parseBigramms(bigramms, true);

		Vector<String> encryptedMessageVariants = getVariants(encryptedMessageVariantsInBigramms);

		StringBuilder encryptedMessage = new StringBuilder();
		for (String variant :
				encryptedMessageVariants) {
			encryptedMessage.append(variant).append("\n");
		}

		return encryptedMessage.toString();
	}

	@Override
	public String decrypt(String message) {
		message = message.trim();
		List<Bigramm> bigramms = splitToBigramms(message);
		List<List<Bigramm>> decryptedMessageVariantsInBigramms = parseBigramms(bigramms, false);

		StringBuilder decryptedMessage = new StringBuilder();
		Vector<String> decryptedMessageVariants = getVariants(decryptedMessageVariantsInBigramms);
		for (String variant :
				decryptedMessageVariants) {
			decryptedMessage.append(variant).append("\n");
		}

		return decryptedMessage.toString();
	}

	private List<List<Bigramm>> parseBigramms(List<Bigramm> bigramms, boolean isEncryption) {
		List<List<Bigramm>> bigrammVariantsList = new ArrayList<>();
		int offset = isEncryption ? 1 : -1;
		for (Bigramm bigramm :
				bigramms) {
			int i1New, i2New, j1New, j2New;
			Pair<Integer, Integer> firstLetterPosition = this.matrix.findSymbolPosition(bigramm.getFirstLetter());
			Pair<Integer, Integer> secondLetterPosition = this.matrix.findSymbolPosition(bigramm.getSecondLetter());
			if (sameRow(firstLetterPosition, secondLetterPosition)) {
				i1New = firstLetterPosition.getKey();
				i2New = secondLetterPosition.getKey();
				j1New = firstLetterPosition.getValue() + offset;
				j2New = secondLetterPosition.getValue() + offset;
			} else if (sameColumn(firstLetterPosition, secondLetterPosition)) {
				i1New = firstLetterPosition.getKey() + offset;
				i2New = secondLetterPosition.getKey() + offset;
				j1New = firstLetterPosition.getValue();
				j2New = secondLetterPosition.getValue();
			} else { // Прямоугольник
				i1New = firstLetterPosition.getKey();
				i2New = secondLetterPosition.getKey();
				j1New = secondLetterPosition.getValue();
				j2New = firstLetterPosition.getValue();
			}
			List<Character> firstLetters = this.matrix.get(i1New, j1New);
			List<Character> secondLetters = this.matrix.get(i2New, j2New);

			this.addCharacters(bigrammVariantsList, firstLetters, secondLetters);
		}
		return bigrammVariantsList;
	}

	private void addCharacters(List<List<Bigramm>> bigrammsVariantList, List<Character> firstLetters, List<Character> secondLetters) {
		bigrammsVariantList.add(new ArrayList<>());
		int lastIndex = Math.max(0, bigrammsVariantList.size() - 1);
		List<Bigramm> possibleBigramms = bigrammsVariantList.get(lastIndex);

		for (char possibleFirstLetter :
				firstLetters) {
			for (char possibleSecondLetter :
				 secondLetters) {
				possibleBigramms.add(new Bigramm(possibleFirstLetter, possibleSecondLetter));
			}
		}
	}

	private Vector<String> getVariants(List<List<Bigramm>> messageVariations) {
		Vector<String> possibleVariants = new Vector<>();
		possibleVariants.add("");
		for (List<Bigramm> bigramm :
				messageVariations) {
			String[] stringVariantsOfBiagramm = biagrammsToString(bigramm);
			String[] oldVariants = new String[possibleVariants.size()];
			possibleVariants.copyInto(oldVariants);

			Vector<String> newPossibleVariants = new Vector<>();
			for (String biagramm :
					stringVariantsOfBiagramm) {
				for (String oldVariant :
						oldVariants) {
					newPossibleVariants.add(oldVariant + biagramm);
				}
			}
			possibleVariants = newPossibleVariants;
		}
		return possibleVariants;
	}
}
