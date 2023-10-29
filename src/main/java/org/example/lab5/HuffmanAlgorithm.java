package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

public class HuffmanAlgorithm implements Encoder, Decoder {
	private static final String DEFAULT_MESSAGE = "test";
	private final BinaryLetterTree letterTree;
	public static void main(String[] args) {
		String message = "BANANA";
		HuffmanAlgorithm compressor = new HuffmanAlgorithm(message);
		String encodedMessage = compressor.encode(message);
		String decodedMessage = compressor.decode(encodedMessage);
		System.out.println("Message: " + message);
		System.out.println("Encoded message: " + encodedMessage);
		System.out.println("Decoded message: " + decodedMessage);
	}
	HuffmanAlgorithm(String message) {
		this.letterTree = new BinaryLetterTree(message);
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
