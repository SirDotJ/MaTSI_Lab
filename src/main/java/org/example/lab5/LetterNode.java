package org.example.lab5;

public class LetterNode implements Comparable<LetterNode> {
	LetterNode up = null;
	LetterNode left = null;
	LetterNode right = null;
	final int value;
	final Character letter;
	public LetterNode(int value, Character letter) {
		this.value = value;
		this.letter = letter;
	}
	public LetterNode(int value) {
		this(value, null);
	}

	public void setUp(LetterNode up) {
		this.up = up;
	}

	public void setLeft(LetterNode left) {
		this.left = left;
	}

	public void setRight(LetterNode right) {
		this.right = right;
	}

	public LetterNode getLeft() {
		return this.left;
	}

	public LetterNode getRight() {
		return right;
	}

	public int getValue() {
		return value;
	}

	public Character getLetter() {
		return letter;
	}

	@Override
	public String toString() {
		return ((this.letter == null ? "Na" : (this.letter)) + ": " + this.value);
	}

	@Override
	public int compareTo(LetterNode letterNode) {
		boolean areEqual = this.getValue() == letterNode.getValue();
		if (areEqual)
			return 0;

		return this.getValue() > letterNode.getValue() ? 1 : -1;
	}
}
