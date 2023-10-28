package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

public class HuffmanCompressor implements Encoder, Decoder {
	public static void main(String[] args) {
		String message = "BANANA";
		HuffmanCompressor compressor = new HuffmanCompressor(message);
		String encodedMessage = compressor.encode(message);
		String decodedMessage = compressor.decode(encodedMessage);
		System.out.println("Message: " + message);
		System.out.println("Encoded message: " + encodedMessage);
		System.out.println("Decoded message: " + decodedMessage);
	}
	private static final String DEFAULT_MESSAGE = "test";
	private final BinaryLetterTree letterTree;
	HuffmanCompressor(String message) {
		this.letterTree = new BinaryLetterTree(message);
	}
	HuffmanCompressor() {
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
