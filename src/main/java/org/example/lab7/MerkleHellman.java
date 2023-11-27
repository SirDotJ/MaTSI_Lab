package org.example.lab7;

import org.example.common.*;

import java.math.BigInteger;
import java.util.*;

/* Криптосистема Меркла-Хеллмана */
public class MerkleHellman implements Encryptor, Decryptor {
	private final static String DELIMITER = "="; // Используется при соответствии интерфейсу передачи при помощи String
	private final static Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
	private final static int DEFAULT_BIT_LENGTH = 8;
	private final static List<Integer> DEFAULT_W = List.of(1, 3, 8, 20, 47, 83, 181, 349);

	public static void main(String[] args) {
		MerkleHellman encryptor = new MerkleHellman();
		String message = "у_нас_в_холодильнике_есть_яйца";
		String encryptedMessage = encryptor.encrypt(message);
		String decryptedMessage = encryptor.decrypt(encryptedMessage);

		System.out.println("Message: " + message);
		System.out.println("Encrypted message: " + encryptedMessage);
		System.out.println("Decrypted message: " + decryptedMessage);
	}
	private final Alphabet alphabet;
	private final int bitLength;
	private final MerkleHellmanPrivateKey privateKey;
	private final List<Integer> publicKey;
	public MerkleHellman(Alphabet alphabet, int bitLength, List<Integer> W) {
		this.alphabet = alphabet;
		this.bitLength = bitLength;
		this.privateKey = new MerkleHellmanPrivateKey(W);
		this.publicKey = calculatePublicKey(this.privateKey);
	}
	public MerkleHellman(Alphabet alphabet, List<Integer> W) {
		this(alphabet, DEFAULT_BIT_LENGTH, W);
	}
	public MerkleHellman(Alphabet alphabet, int bitLength) {
		this(alphabet, bitLength, DEFAULT_W);
	}
	public MerkleHellman(List<Integer> W) {
		this(DEFAULT_ALPHABET, DEFAULT_BIT_LENGTH, W);
	}
	public MerkleHellman(int bitLength) {
		this(DEFAULT_ALPHABET, bitLength, DEFAULT_W);
	}
	public MerkleHellman(Alphabet alphabet) {
		this(alphabet, DEFAULT_BIT_LENGTH, DEFAULT_W);
	}
	public MerkleHellman() {
		this(DEFAULT_ALPHABET, DEFAULT_BIT_LENGTH, DEFAULT_W);
	}

	private static List<Integer> calculatePublicKey(MerkleHellmanPrivateKey privateKey) {
		BigInteger bigR = BigInteger.valueOf(privateKey.getR());
		BigInteger bigQ = BigInteger.valueOf(privateKey.getQ());

		List<Integer> B = new ArrayList<>();
		for (int w : privateKey.getW()) {
			BigInteger bigW = BigInteger.valueOf(w);
			/* b_i = (rw_i) mod q */
			B.add(bigR.multiply(bigW).mod(bigQ).intValue());
		}

		return Collections.unmodifiableList(B);
	}

	@Override
	public String encrypt(String message) {
		List<Integer> numericalMessage = this.alphabet.convert(message);

		List<BinaryNumber> binaryMessage = new ArrayList<>();
		for (int block : numericalMessage)
			binaryMessage.add(new BinaryNumber(block + 2, this.bitLength)); // + 2 для избежания нулевых и единичных значений

		List<Integer> encryptedMessage = new ArrayList<>();
		for (BinaryNumber binaryBlock : binaryMessage)
			encryptedMessage.add((int) binaryBlock.bitwiseAndMultiply(this.publicKey));

		return packToString(encryptedMessage);
	}

	@Override
	public String decrypt(String message) {
		List<Integer> numericalMessage = unpackString(message);

		List<Integer> decryptedMessage = new ArrayList<>();
		for (int encryptedBlock : numericalMessage)
			decryptedMessage.add(
				applyBackpackSolution(
						backpackPackingSolver(
								this.privateKey.getW(),
								(this.privateKey.getInverseR() * encryptedBlock) % this.privateKey.getQ()
						),
						this.bitLength
				)
			);

		return this.alphabet.convert(decryptedMessage);
	}

	public static int applyBackpackSolution(List<Integer> solution, int n) {
		Set<Integer> specialValues = new HashSet<>(solution);
		int sum = 0;
		for (Integer x : specialValues) {
			sum += Math.pow(2, (n - x - 1));
		}
		return sum - 2; // - 2 для учета проведенной манипуляции в encrypt
	}

	public static List<Integer> backpackPackingSolver(List<Integer> multitude, int targetNumber) {
		List<Integer> indexes = new ArrayList<>();
		int currentTargetNumber = targetNumber;
		do {
			int closestLowestIndex = 0;
			int closestLowest = multitude.get(0);
			for (int i = 1; i < multitude.size(); i++) {
				int value = multitude.get(i);
				if (value > currentTargetNumber)
					break;
				else {
					closestLowestIndex = i;
					closestLowest = value;
				}
			}
			indexes.add(closestLowestIndex);
			currentTargetNumber -= closestLowest;
		} while (currentTargetNumber > 0);
		if (currentTargetNumber == 0)
			return indexes;
		throw new RuntimeException("No solution could be found for backpack packing");
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
