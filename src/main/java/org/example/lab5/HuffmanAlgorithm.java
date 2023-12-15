package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

public class HuffmanAlgorithm implements Encoder, Decoder {
	private static final String DEFAULT_MESSAGE = "test";
	private final BinaryWordTree letterTree;

	HuffmanAlgorithm(String message) {
		this.letterTree = new BinaryWordTree(message);
	}
	public HuffmanAlgorithm() {
		this(DEFAULT_MESSAGE);
	}

	public void initializeTree(String message) {
		this.letterTree.initialize(message);
	}

	@Override
	public String encode(String plainMessage) {
		this.letterTree.initialize(plainMessage);
		return letterTree.encodeText(plainMessage);
	}

	@Override
	public String decode(String encodedMessage) {
		return this.letterTree.decodeText(encodedMessage);
	}
}
