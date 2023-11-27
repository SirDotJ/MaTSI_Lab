package org.example.lab7;

import org.example.common.Alphabet;
import org.example.common.AlphabetConstants;
import org.example.common.Decryptor;
import org.example.common.Encryptor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/* Криптосистема Рабина */
public class Rabin implements Encryptor, Decryptor {
	final static private String DELIMITER = "="; // Используется при соответствии интерфейсу передачи при помощи String
	final static private Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;

	/* Из примера */
	private static final int DEFAULT_MARKER = 77;
	private static final int DEFAULT_PRIME_1 = 5923;
	private static final int DEFAULT_PRIME_2 = 5867;

	private Alphabet alphabet;
	private RabinPrivateKey privateKey;
	private int publicKey;
	private int marker;
	public Rabin(Alphabet alphabet, int prime1, int prime2, int marker) throws IllegalArgumentException {
		if (!keysValid(prime1, prime2))
			throw new IllegalArgumentException("Provided keys are not valid for Rabin crypto-system");

		this.alphabet = alphabet;
		this.privateKey = new RabinPrivateKey(prime1, prime2);
		this.publicKey = prime1 * prime2;
		this.marker = marker;
	}

	public Rabin(Alphabet alphabet, int prime1, int prime2) {
		this(alphabet, prime1, prime2, DEFAULT_MARKER);
	}

	public Rabin(int prime1, int prime2) {
		this(DEFAULT_ALPHABET, prime1, prime2, DEFAULT_MARKER);
	}

	public Rabin() {
		this(DEFAULT_ALPHABET, DEFAULT_PRIME_1, DEFAULT_PRIME_2, DEFAULT_MARKER);
	}
	public void setNewAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }
    public void setNewKey(int prime1, int prime2, int marker) throws IllegalArgumentException {
        if (!keysValid(prime1, prime2))
			throw new IllegalArgumentException("Provided keys are not valid for Rabin crypto-system");

		this.privateKey = new RabinPrivateKey(prime1, prime2);
		this.publicKey = prime1 * prime2;
		this.marker = marker;
    }

	private static boolean keysValid(int prime1, int prime2) {
		return  (prime1 % 4 == 3) &&
				(prime2 % 4 == 3);
	}

	@Override
	public String encrypt(String message) {
		List<Integer> numericalMessage = this.alphabet.convert(message);
		List<Integer> encryptedMessage = this.encrypt(numericalMessage);
		return packToString(encryptedMessage);
	}

	private List<Integer> encrypt(List<Integer> message) {
		List<Integer> markedMessage = markMessage(message, this.marker);
		List<Integer> encryptedMessage = new ArrayList<>();

		for (int block : markedMessage)
			encryptedMessage.add(encryptBlock(block, this.publicKey));

		return encryptedMessage;
	}

	public static List<Integer> markMessage(List<Integer> unmarkedMessage, int marker) {
		List<Integer> markedMessage = new ArrayList<>();

		for (int unmarkedValue : unmarkedMessage) {
			// + 2 для избежания нулевых и единичных значений
			int unmarkedValueLength = String.valueOf(unmarkedValue + 2).length();
			int markedValue = (unmarkedValue + 2) + (int) (marker * Math.pow(10, unmarkedValueLength));
			markedMessage.add(markedValue);
		}

		return markedMessage;
	}

	public static int encryptBlock(int block, int modValue) {
		// M^2 mod n
		BigInteger power = BigInteger.valueOf(block);
		power = power.pow(2);
		return power.mod(BigInteger.valueOf(modValue)).intValue();
	}

	@Override
	public String decrypt(String message) {
		List<Integer> numericalMessage = unpackString(message);
		List<Integer> decryptedMessage = this.decrypt(numericalMessage);
		return this.alphabet.convert(decryptedMessage);
	}
	public List<Integer> decrypt(List<Integer> encryptedMessage) {
		List<Integer> rValues = calcSquareRootModulo(encryptedMessage, this.privateKey.getPrime1());
		List<Integer> sValues = calcSquareRootModulo(encryptedMessage, this.privateKey.getPrime2());

		List<Integer> decryptedMessage = new ArrayList<>();
		for (int i = 0; i < encryptedMessage.size(); i++) {
			int rValue = rValues.get(i);
			int sValue = sValues.get(i);

			List<Integer> variants = calculateDecryptionVariants(
					sValue, this.privateKey.getBezoutCoefficient1(), this.privateKey.getPrime1(),
					rValue, this.privateKey.getBezoutCoefficient2(), this.privateKey.getPrime2(),
					this.publicKey);
			decryptedMessage.add(findRealDecryption(variants));
		}

		return decryptedMessage;
	}

	public static List<Integer> calcSquareRootModulo(List<Integer> values, int modulo) {
		List<Integer> output = new ArrayList<>();

		BigInteger bigModulo = BigInteger.valueOf(modulo);
		for (int value : values) {
			BigInteger bigValue = BigInteger.valueOf(value);
			BigInteger power = bigValue.pow((modulo + 1) / 4);
			output.add(power.mod(bigModulo).intValue());
		}

		return output;
	}

	public static List<Integer> calculateDecryptionVariants(int sValue, int bezoutCoefficient1, int prime1, int rValue, int bezoutCoefficient2, int prime2, int publicKey) {
		BigInteger bigSValue = BigInteger.valueOf(sValue);
		BigInteger bigBezoutCoefficient1 = BigInteger.valueOf(bezoutCoefficient1);
		BigInteger bigPrime1 = BigInteger.valueOf(prime1);
		BigInteger bigRValue = BigInteger.valueOf(rValue);
		BigInteger bigBezoutCoefficient2 = BigInteger.valueOf(bezoutCoefficient2);
		BigInteger bigPrime2 = BigInteger.valueOf(prime2);
		BigInteger bigPublicKey = BigInteger.valueOf(publicKey);

		List<Integer> variants = new ArrayList<>();
		BigInteger multiplyB1P1S = bigBezoutCoefficient1.multiply(bigPrime1).multiply(bigSValue);
		BigInteger multiplyB2P2R = bigBezoutCoefficient2.multiply(bigPrime2).multiply(bigRValue);
		int variant1 = multiplyB1P1S.add(multiplyB2P2R).mod(bigPublicKey).intValue();
		int variant2 = publicKey - variant1;
		int variant3 = multiplyB1P1S.subtract(multiplyB2P2R).mod(bigPublicKey).intValue();
		int variant4 = publicKey - variant3;
		variants.add(variant1);
		variants.add(variant2);
		variants.add(variant3);
		variants.add(variant4);
		return variants;
	}

	private int findRealDecryption(List<Integer> variants) throws IllegalArgumentException {
		int marker = this.marker;
		int markerLength = String.valueOf(marker).length();

		for (int variant :
				variants) {
			int variantLength = String.valueOf(variant).length();
			int mask = (int) Math.pow(10, variantLength - markerLength);
			if (mask < 1) // Меньше маски, даже проверять не надо
				continue;

			int supposedValue = variant % mask;
			int markerValues = (variant - supposedValue) / mask;
			if (markerValues == marker)
				return supposedValue - 2; // - 2 для учета проведенной манипуляции в markMessage
		}

		throw new IllegalArgumentException("No valid decryption variant found");
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

	/* WARNING: FOR DEMONSTRATION PURPOSES ONLY */
	public int getPublicKey() {
		return publicKey;
	}
	public RabinPrivateKey getPrivateKey() {
		return privateKey;
	}
}
