package org.togu.lab5;

import java.util.ArrayList;
import java.util.List;

public class HemmingSecuredMessage {
	private List<BinaryWord> message;
	private int subdivisionCount;
	HemmingSecuredMessage(String plainMessage, int subdivisionCount) {
		this.initializeMessage(plainMessage, subdivisionCount);
	}

	void initializeMessage(String plainMessage, int subdivisionCount) {
		this.subdivisionCount = subdivisionCount;
		this.message = new ArrayList<>();
		List<Character> word = new ArrayList<>();

		int size = plainMessage.length();
		for (int i = 0; i < size; i++) {
			char letter = plainMessage.charAt(i);
			word.add(letter);
			if (word.size() >= this.subdivisionCount) {
				StringBuilder part = new StringBuilder();
				for (Character character : word)
					part.append(character);
				this.message.add(HemmingCode.secureWord(part.toString()));
				word = new ArrayList<>();
			}
		}

		if (!word.isEmpty()) {
			StringBuilder lastPart = new StringBuilder();
			for (Character character : word)
				lastPart.append(character);
			this.message.add(HemmingCode.secureWord(lastPart.toString()));
		}
	}

	public String getCleanMessage() {
		StringBuilder cleanMessage = new StringBuilder();
		for (BinaryWord word :
				this.message) {
			cleanMessage.append(word.getCleanWord());
		}
		return cleanMessage.toString();
	}

	public BinaryWord get(int index) {
		return this.message.get(index);
	}

	public int size() {
		return this.message.size();
	}

	public int[] check() {
		int[] errorPositions = new int[this.size()];
		for (int i = 0; i < this.size(); i++) {
			errorPositions[i] = this.check(i);
		}
		return errorPositions;
	}

	public int check(int index) {
		return HemmingCode.checkWord(this.message.get(index));
	}

	@Override
	public String toString() {
		StringBuilder fullMessage = new StringBuilder();
		for (BinaryWord word:
			 this.message) {
			fullMessage.append(word.toString()).append("_");
		}
		fullMessage.deleteCharAt(fullMessage.length() - 1);
		return fullMessage.toString();
	}
}
