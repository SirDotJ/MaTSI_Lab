package org.togu.lab4;

import java.util.LinkedList;
import java.util.List;

public class AlphabetMatrixEntry {
	private final List<Character> characters;
	public AlphabetMatrixEntry() {
		this.characters = new LinkedList<>();
	}
	public AlphabetMatrixEntry(List<Character> characters) {
		this.characters = new LinkedList<>(characters);
	}
	public AlphabetMatrixEntry(char character) {
		this.characters = new LinkedList<>(List.of(character));
	}
	public List<Character> getCharacters() {
		return characters;
	}
	public void add(char character) {
		this.characters.add(character);
	}
}
