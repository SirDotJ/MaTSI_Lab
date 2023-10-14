package org.example.lab3;

import org.example.common.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixMultiplicationEncryptor implements Encryptor, Decryptor {
	private static final String ROW_SEPARATOR = "\n";
    private static final String COLUMN_SEPARATOR = " ";
    private static final Alphabet DEFAULT_ALPHABET = AlphabetConstants.FULL_WITH_SPACE;
    private static final boolean DEFAULT_LETTER_IS_LOWER = true; // в каком регистре выводится результат расшифровки
    private static final ArrayList<int[]> DEFAULT_KEY = new ArrayList<>(Arrays.asList(
            new int[] {1, 3, 5},
            new int[] {6, 2, 4},
            new int[] {9, 1, 3}
    ));

    private final Alphabet alphabet;
    private ArrayList<int[]> key = new ArrayList<>();

    public MatrixMultiplicationEncryptor(Alphabet alphabet, ArrayList<int[]> key) {
        this.alphabet = alphabet;
        this.setKey(key);
    }
    public MatrixMultiplicationEncryptor(Alphabet alphabet) {
        this(alphabet, DEFAULT_KEY);
    }
    public MatrixMultiplicationEncryptor(ArrayList<int[]> key) {
        this(DEFAULT_ALPHABET, key);
    }
    public MatrixMultiplicationEncryptor(String key) {
        this(DEFAULT_ALPHABET, parseKey(key));
    }

    public void setKey(ArrayList<int[]> key) throws IllegalArgumentException {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("No key passed");

        int columnSize = key.get(0).length;
        for (int[] row : key) {
            if(row.length != columnSize)
                throw new IllegalArgumentException("Passed key's column sizes are not consistent");
        }

        int rowSize = key.size();
        if (rowSize != columnSize) {
            throw new IllegalArgumentException("Passed key is not a square matrix");
        }

        int determinant = MatrixMath.matrixDeterminant(key);
        if (determinant == 0)
            throw new IllegalArgumentException("Passed key's determinant value is equal to 0");

        this.key = key;
    }
    public void setKey(String key) {
        this.setKey(parseKey(key));
    }
    public static ArrayList<int[]> parseKey(String key) {
        ArrayList<int[]> parsedKey = new ArrayList<>();
        String[] inputRows = key.split(ROW_SEPARATOR);
        for (int i = 0; i < inputRows.length; i++) {
            String[] inputRowNumbers = inputRows[i].split(COLUMN_SEPARATOR);
            parsedKey.add(new int[inputRowNumbers.length]);
            for (int j = 0; j < inputRowNumbers.length; j++) {
                parsedKey.get(i)[j] = Integer.parseInt(inputRowNumbers[j]);
            }
        }

        return parsedKey;
    }

    private int[] convertMessageToNumerical(String message) {
        int[] numbers = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            char letter = message.charAt(i);
            int letterIndex = this.alphabet.indexOf(letter) + 1;
            numbers[i] = letterIndex;
        }

        return numbers;
    }
    // Так как передается только числа, то нельзя узнать, в каком регистре были изначальные буквы
    private String convertNumbersToMessage(int[] numbers) {
        StringBuilder message = new StringBuilder();
        for (int letterIndex : numbers) {
            char letter = this.alphabet.get(letterIndex - 1);
            letter = DEFAULT_LETTER_IS_LOWER ? Character.toLowerCase(letter)
                                             : Character.toUpperCase(letter);
            message.append(letter);
        }

        return message.toString();
    }
    private static int[] readEncryptedMessageToMassive(String encryptedMessage) {
        String[] numbers = encryptedMessage.split(" ");
        int[] numericalEncryptedMessage = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            String number = numbers[i];
            numericalEncryptedMessage[i] = Integer.parseInt(number);
        }

        return numericalEncryptedMessage;
    }

    @Override
    public String encrypt (String message) throws IllegalArgumentException {
        if (message == null || message.isEmpty())
            throw new IllegalArgumentException("Passed message is empty");

        StringBuilder openMessage = new StringBuilder(String.copyValueOf(message.toCharArray()));
        int rowCount = this.key.size();
        if (openMessage.length() % rowCount != 0) {
            int difference = rowCount - openMessage.length() % rowCount;
            openMessage.append(String.valueOf(AlphabetConstants.SPACE_CHARACTER).repeat(difference));
        }

        int[] numericalMessage = this.convertMessageToNumerical(openMessage.toString());
        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < numericalMessage.length / rowCount; i++) {
            int indexOffset = i * rowCount;
            ArrayList<int[]> messagePart = new ArrayList<>();
            for (int j = 0; j < rowCount; j++) {
                int[] number = new int[] {numericalMessage[indexOffset + j]};
                messagePart.add(number);
            }

            List<int[]> encryptedMessagePart = MatrixMath.multiplyMatrices(this.key, messagePart);
            for (int[] number : encryptedMessagePart) {
                encryptedMessage.append(number[0]).append(" ");
            }
        }

        return encryptedMessage.toString();
    }

    @Override
    public String decrypt (String encryptedMessage) throws IllegalArgumentException {
        if (encryptedMessage == null || encryptedMessage.isEmpty())
            throw new IllegalArgumentException("Passed message is empty");

        int[] numericalEncryptedMessage = readEncryptedMessageToMassive(encryptedMessage);
        int messageRowCount = this.key.size();
        if (numericalEncryptedMessage.length % messageRowCount != 0)
            throw new IllegalArgumentException("Provided message cannot be decrypted using current key");

        int determinant = MatrixMath.matrixDeterminant(this.key);
        List<int[]> decryptionKeyPart = MatrixMath.attachedMatrix(this.key); // деление на определитель будет проведено после умножения
        decryptionKeyPart = MatrixMath.transposeMatrix(decryptionKeyPart);

        int[] numericalDecryptedMessage = new int[numericalEncryptedMessage.length];
        for (int i = 0; i < numericalEncryptedMessage.length / messageRowCount; i++) {
            int indexOffset = i * messageRowCount;
            ArrayList<int[]> messagePart = new ArrayList<>();
            for (int j = 0; j < messageRowCount; j++) {
                int[] number = new int[] {numericalEncryptedMessage[indexOffset + j]};
                messagePart.add(number);
            }

            List<int[]> semiDecryptedMessagePart = MatrixMath.multiplyMatrices(decryptionKeyPart, messagePart);
            List<int[]> decryptedMessagePart = MatrixMath.divideAllMatrixElements(semiDecryptedMessagePart, determinant);
            for (int j = 0; j < messageRowCount; j++) {
                numericalDecryptedMessage[indexOffset + j] = decryptedMessagePart.get(j)[0];
            }
        }

        return this.convertNumbersToMessage(numericalDecryptedMessage);
    }
}
