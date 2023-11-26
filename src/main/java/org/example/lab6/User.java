package org.example.lab6;

import org.example.common.Decryptor;
import org.example.common.Encryptor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class User implements Encryptor, Decryptor {
	final static private String DELIMITER = "="; // Используется при соответствии интерфейсу передачи при помощи String

	private final ElGamal network;
	private final int privateKey;
	private final int publicKey;
	private User connection = null; // используется для обхода ограничений с интерфейсом Encryptor и Decryptor
	public User(ElGamal network) {
		this.network = network;
		this.privateKey = generatePrivateKey(network.getNetworkKey1());
		this.publicKey = calculatePublicKey(this.network, this.privateKey);
	}
	private static int generatePrivateKey(int ceiling) {
		return (int) (Math.random() * (ceiling - 2) + 2); // 1 < key < ceiling - 1
	}
	private static int calculatePublicKey(ElGamal network, int privateKey) {
        BigInteger bigPrime1 = new BigInteger(String.valueOf(network.getNetworkKey1()));
        BigInteger bigPrime2 = new BigInteger(String.valueOf(network.getNetworkKey2()));
		/* Y_i = A^Xi mod P */
        return (bigPrime2.pow(privateKey).mod(bigPrime1).intValue());
	}

	public int getPublicKey() {
		return publicKey;
	}

	public String sendMessage(User receiver, String message) {
		this.connection = receiver;
		return encrypt(message);
	}
	public String sendMessage(String message) throws IllegalStateException {
		if (this.connection == null)
			throw new IllegalStateException("No connection set");
		return encrypt(message);
	}
	public String receiveMessage(User sender, String encryptedMessage) {
		this.connection = sender;
		return decrypt(encryptedMessage);
	}
	public String receiveMessage(String encryptedMessage) throws IllegalStateException {
		if (this.connection == null)
			throw new IllegalStateException("No connection set");
		return decrypt(encryptedMessage);
	}
	@Override
	public String encrypt(String message) {
		User receiver = this.connection;
		List<Integer> numericalMessage = this.network.getAlphabet().convert(message);

		List<Integer> encryptedMessage = new ArrayList<>();
		for (int block : numericalMessage)
			encryptedMessage.add(encryptBlock(block, this, receiver));

		return packToString(encryptedMessage);
	}

	private static int encryptBlock(int block, User sender, User receiver) {
        BigInteger bigBlock = BigInteger.valueOf(block + 2); // + 2 для избежания нулевых и единичных значений
        BigInteger bigNetworkKey1 = BigInteger.valueOf(sender.network.getNetworkKey1());
        BigInteger bigPublicReceiverKey = BigInteger.valueOf(receiver.getPublicKey());

		/* (Mi * Yr^Xs) mod P */
        BigInteger power = bigPublicReceiverKey.pow(sender.privateKey);
        BigInteger multiplication = bigBlock.multiply(power);
        BigInteger remainder = multiplication.mod(bigNetworkKey1);
        return remainder.intValue();
	}

	@Override
	public String decrypt(String message) {
		User sender = this.connection;
		List<Integer> numericalMessage = unpackString(message);

		List<Integer> decryptedMessage = new ArrayList<>();
		for (int encryptedBlock : numericalMessage)
			decryptedMessage.add(decryptBlock(encryptedBlock, sender, this));

		return this.network.getAlphabet().convert(decryptedMessage);
	}

	private static int decryptBlock(int encryptedBlock, User sender, User receiver) {
        BigInteger bigEncryptedBlock = BigInteger.valueOf(encryptedBlock);
        BigInteger bigNetworkKey1 = BigInteger.valueOf(sender.network.getNetworkKey1());
        BigInteger bigPublicSenderKey = BigInteger.valueOf(sender.getPublicKey());

		/* M(i) = e * r^{P - 1 - Xi} mod P */
        BigInteger power = bigPublicSenderKey.pow(sender.network.getNetworkKey1() - 1 - receiver.privateKey);
        BigInteger multiplication = power.multiply(bigEncryptedBlock);
        BigInteger remainder = multiplication.mod(bigNetworkKey1);
        return remainder.intValue() - 2; // - 2 для учета проведенной манипуляции в encryptBlock
	}

	/* Используются для соответствия схеме шифровки в сообщения String формата */
    private String packToString(List<Integer> message) {
        StringBuilder builder = new StringBuilder();
        builder.append(message.get(0));
        for (int i = 1; i < message.size(); i++)
            builder.append(DELIMITER).append(message.get(i));
        return builder.toString();
    }
    private List<Integer> unpackString(String message) {
        String[] blocks = message.split(DELIMITER);
        List<Integer> parsed = new ArrayList<>();
        for (String block : blocks) {
            parsed.add(Integer.parseInt(block));
        }
        return parsed;
    }
}
