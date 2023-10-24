package org.example.lab4;

import javafx.util.Pair;
import org.example.common.Alphabet;

import java.util.*;

public class AlphabetMatrix {
	private List<List<AlphabetMatrixEntry>> matrix;
	private Alphabet alphabet;
	private int rowCount;
	private int columnCount;

	AlphabetMatrix(String key, Alphabet alphabet) {
		this.alphabet = alphabet;
		this.rowCount = this.alphabet.type() == Alphabet.TYPE.LATIN ? 5 : 4;
		this.columnCount = this.alphabet.type() == Alphabet.TYPE.LATIN ? 5 : 8;
		this.initializeMatrix(key);
	}
	public void initializeMatrix(String key, Alphabet alphabet) {
		this.alphabet = alphabet;
		this.rowCount = this.alphabet.type() == Alphabet.TYPE.LATIN ? 5 : 4;
		this.columnCount = this.alphabet.type() == Alphabet.TYPE.LATIN ? 5 : 8;
		this.initializeMatrix(key);
	}
	public void initializeMatrix(String key) {
		key = key.toLowerCase();
		// Make an empty matrix
		this.matrix = new ArrayList<>();
		for (int i = 0; i < this.rowCount; i++) {
			this.matrix.add(new ArrayList<>());
			for (int j = 0; j < this.columnCount; j++) {
				this.matrix.get(i).add(new AlphabetMatrixEntry());
			}
		}
		// Fill matrix from key
		Set<Character> usedLetters = new HashSet<>();
		int addedLettersCounter = 0;
		for (int i = 0; i < key.length(); i++) {
			char letter = key.charAt(i);
			if (usedLetters.contains(letter))
				continue;
			usedLetters.add(letter);
			this.addCharTo(addedLettersCounter++, letter);
			Alphabet.TYPE currentType = this.alphabet.type();
			// Проверка на i или j для латиницы и ь или ъ для кириллицы
			if (currentType == Alphabet.TYPE.LATIN && (letter == 'i' || letter == 'j')) {
				if (letter == 'i') {
					usedLetters.add('j');
					this.addCharTo(addedLettersCounter - 1, 'j');
				} else {
					usedLetters.add('i');
					this.addCharTo(addedLettersCounter - 1, 'i');
				}
			} else if (currentType == Alphabet.TYPE.CYRILLIC && (letter == 'ь' || letter == 'ъ')) {
				if (letter == 'ь') {
					usedLetters.add('ъ');
					this.addCharTo(addedLettersCounter - 1, 'ъ');
				} else {
					usedLetters.add('ь');
					this.addCharTo(addedLettersCounter - 1, 'ь');
				}
			}
		}

		// Fill matrix from remainder alphabet
		for (int i = 0; i < this.alphabet.size(); i++) {
			char letter = this.alphabet.get(i);
			if (usedLetters.contains(letter))
				continue;
			usedLetters.add(letter);
			this.addCharTo(addedLettersCounter++, letter);
			// Проверка на i или j для латиницы и ь или ъ для кириллицы
			Alphabet.TYPE currentType = this.alphabet.type();
			if (currentType == Alphabet.TYPE.LATIN && (letter == 'i' || letter == 'j')) {
				if (letter == 'i') {
					usedLetters.add('j');
					this.addCharTo(addedLettersCounter - 1, 'j');
				} else {
					usedLetters.add('i');
					this.addCharTo(addedLettersCounter - 1, 'i');
				}
			} else if (currentType == Alphabet.TYPE.CYRILLIC && (letter == 'ь' || letter == 'ъ')) {
				if (letter == 'ь') {
					usedLetters.add('ъ');
					this.addCharTo(addedLettersCounter - 1, 'ъ');
				} else {
					usedLetters.add('ь');
					this.addCharTo(addedLettersCounter - 1, 'ь');
				}
			}
		}
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public Alphabet.TYPE getType() {
		return this.alphabet.type();
	}

	public List<Character> get(int i, int j) {
		if (i >= this.rowCount)
			i = Math.abs(i % this.rowCount);
		else if (i < 0)
			i = this.rowCount - Math.abs(i);

		if (j >= this.columnCount)
			j = Math.abs(j % this.columnCount);
		else if (j < 0)
			j = this.columnCount - Math.abs(j);

		return this.matrix.get(i).get(j).getCharacters();
	}

	private void addCharTo(int number, char letter) {
		int i = number / this.columnCount;
		int j = number % this.columnCount;
		this.addCharTo(i, j, letter);
	}
	private void addCharTo(int i, int j, char letter) {
		this.matrix.get(i).get(j).add(letter);
	}

	public Pair<Integer, Integer> findSymbolPosition(char symbol) {
		for (int i = 0; i < this.rowCount; i++) {
			for (int j = 0; j < this.columnCount; j++) {
				List<Character> letter = this.matrix.get(i).get(j).getCharacters();
				if (letter.contains(symbol))
					return new Pair<>(i, j);
			}
		}
		return null;
	}
}
