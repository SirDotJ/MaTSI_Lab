package org.togu.lab6;

import org.togu.common.Alphabet;
import org.togu.common.AlphabetConstants;
import org.togu.common.Decryptor;
import org.togu.common.Encryptor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/* Реализация алгоритма шифровки RSA */
public class RSA implements Encryptor, Decryptor {
    final static private String DELIMITER = "="; // Используется при соответствии интерфейсу передачи при помощи String
    final static private Alphabet DEFAULT_ALPHABET = AlphabetConstants.CYRILLIC_WITH_SPACE;
    /* Из примера */
    final static private int DEFAULT_PRIME_1 = 89;
    final static private int DEFAULT_PRIME_2 = 71;

    private Alphabet alphabet;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSA(Alphabet alphabet, int prime1, int prime2) throws IllegalArgumentException {
        this.alphabet = alphabet;
        try {
            this.publicKey = new RSAPublicKey(prime1, prime2);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided values do not allow for RSAPublicKey to be made!");
        }
        this.privateKey = new RSAPrivateKey(this.publicKey, prime1, prime2);
    }
    public RSA(int prime1, int prime2) {
        this(DEFAULT_ALPHABET, prime1, prime2);
    }
    public RSA() {
        this(DEFAULT_ALPHABET, DEFAULT_PRIME_1, DEFAULT_PRIME_2);
    }
    public void setNewAlphabet(Alphabet alphabet) {
        this.alphabet = alphabet;
    }
    public void setNewKey(int prime1, int prime2) throws IllegalArgumentException {
        RSAPublicKey newPublicKey;
        try {
            newPublicKey = new RSAPublicKey(prime1, prime2);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided values do not allow for RSAPublicKey to be made!");
        }
        this.publicKey = newPublicKey;
        this.privateKey = new RSAPrivateKey(this.publicKey, prime1, prime2);
    }
    @Override
    public String encrypt(String message) {
        List<Integer> numericalMessage = this.alphabet.convert(message);

        int publicExponent = this.publicKey.getPublicExponent();
        int modulus = this.publicKey.getModulus();

        List<Integer> encryptedMessage = new ArrayList<>();
        for (int block : numericalMessage)
            encryptedMessage.add(encryptBlock(block, publicExponent, modulus));

        return packToString(encryptedMessage);
    }
    @Override
    public String decrypt(String message) {
        List<Integer> encryptedMessage = unpackString(message);

        int multiplicativeInverse = this.privateKey.getMultiplicativeInverse();
        int modulus = this.privateKey.getModulus();

        List<Integer> decryptedMessage = new ArrayList<>();
        for (int encryptedBlock : encryptedMessage)
            decryptedMessage.add(decryptBlock(encryptedBlock, multiplicativeInverse, modulus));

        return this.alphabet.convert(decryptedMessage);
    }

    private static int encryptBlock(int block, int publicExponentOrMultiplicativeInverse, int modulus) {
        BigInteger bigBlock = new BigInteger(String.valueOf(block + 2)); // + 2 Для избегания обнуления и отсутствия изменений при первом (0) и втором (1) символах
        BigInteger bigPower = bigBlock.pow(publicExponentOrMultiplicativeInverse);
        BigInteger bigModulus = new BigInteger(String.valueOf(modulus));
        return Integer.parseInt(bigPower.mod(bigModulus).toString());
    }
    private static int decryptBlock(int encryptedBlock, int publicExponentOrMultiplicativeInverse, int modulus) {
        BigInteger bigBlock = new BigInteger(String.valueOf(encryptedBlock));
        BigInteger bigPower = bigBlock.pow(publicExponentOrMultiplicativeInverse);
        BigInteger bigModulus = new BigInteger(String.valueOf(modulus));
        return Integer.parseInt(bigPower.mod(bigModulus).toString()) - 2;// - 2 Для учета соответствующей операции в encryptBlock
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
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    /* WARNING: FOR DEMONSTRATION PURPOSES ONLY */
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }
}
