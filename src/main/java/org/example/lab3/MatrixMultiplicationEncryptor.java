package org.example.lab3;

import org.example.common.Encryptor;
import org.example.common.GlobalVariables;
import org.example.common.MatrixMath;

import java.util.ArrayList;

public class MatrixMultiplicationEncryptor implements Encryptor {
	private ArrayList<int[]> key = new ArrayList<>();
	public MatrixMultiplicationEncryptor(String key) throws NullPointerException, IllegalArgumentException {
        // Парсинг текста в ключ
		String[] inputRows = key.split("\n");
        for (int i = 0; i < inputRows.length; i++) {
            String[] inputRowNumbers = inputRows[i].split(" ");
            this.key.add(new int[inputRowNumbers.length]);
            for (int j = 0; j < inputRowNumbers.length; j++) {
                this.key.get(i)[j] = Integer.parseInt(inputRowNumbers[j]);
            }
        }

        // Проверка результата парсинга
        if (this.key == null)
            throw new NullPointerException("Key matrix was not made");

        int columnSize = this.key.get(0).length;
        for (int[] row :
                this.key) {
            if(row.length != columnSize)
                throw new IllegalArgumentException("Resulting key matrix is invalid: column size is not consistent");
        }

        int rowSize = this.key.size();
        if (rowSize != columnSize) {
            throw new IllegalArgumentException("Resulting key matrix is not a square matrix");
        }
	}

    private static int[] convertMessageToNumerical(String message) {
        int[] numbers = new int[message.length()];

        for (int i = 0; i < message.length(); i++) {
            char letter = message.charAt(i);
            int letterIndex = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.indexOf(Character.toUpperCase(letter)) + 1;
            numbers[i] = letterIndex;
        }

        return numbers;
    }

    private static String convertNumbersToMessage(int[] numbers) {
        StringBuilder message = new StringBuilder();

        for (int letterIndex : numbers) {
            char letter = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(letterIndex - 1);
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

    public String encrypt (String message) {
        StringBuilder openMessage = new StringBuilder(String.copyValueOf(message.toCharArray()));
        int rowCount = this.key.size();
        if (openMessage.length() % rowCount != 0) {
            for (int i = 0; i < openMessage.length() % rowCount; i++) {
                openMessage.append(GlobalVariables.SPACE_CHARACTER);
            }
        }
        int[] numericalMessage = convertMessageToNumerical(openMessage.toString());

        StringBuilder encryptedMessage = new StringBuilder();
        for (int i = 0; i < numericalMessage.length / rowCount; i++) {
            int indexOffset = i * rowCount;
            ArrayList<int[]> messagePart = new ArrayList<>();
            for (int j = 0; j < rowCount; j++) {
                int[] number = new int[] {numericalMessage[indexOffset + j]};
                messagePart.add(number);
            }

            ArrayList<int[]> encryptedMessagePart = MatrixMath.multiplyMatrices(this.key, messagePart);
            for (int[] number : encryptedMessagePart) {
                encryptedMessage.append(number[0]).append(" ");
            }
        }

        return encryptedMessage.toString();
    }

    public String decrypt (String encryptedMessage) throws IllegalArgumentException {
        int[] numericalEncryptedMessage = readEncryptedMessageToMassive(encryptedMessage);
        int rowCount = this.key.size();
        if (numericalEncryptedMessage.length % rowCount != 0)
            throw new IllegalArgumentException("Provided message cannot be decrypted using current key");

        int determinant = MatrixMath.matrixDeterminant(this.key);
        if (determinant == 0)
            throw new IllegalArgumentException("Key cannot be used for decryption: matrix determinant is equal to 0, hence no inverse matrix can be made");

        ArrayList<int[]> decryptionKeyPart = MatrixMath.attachedMatrix(this.key); // деление на определитель будет проведено после умножения
        decryptionKeyPart = MatrixMath.transposeMatrix(decryptionKeyPart);


        int[] numericalDecryptedMessage = new int[numericalEncryptedMessage.length];
        for (int i = 0; i < numericalEncryptedMessage.length / rowCount; i++) {
            int indexOffset = i * rowCount;
            ArrayList<int[]> messagePart = new ArrayList<>();
            for (int j = 0; j < rowCount; j++) {
                int[] number = new int[] {numericalEncryptedMessage[indexOffset + j]};
                messagePart.add(number);
            }

            ArrayList<int[]> semiDecryptedMessagePart = MatrixMath.multiplyMatrices(decryptionKeyPart, messagePart);

            ArrayList<int[]> decryptedMessagePart = MatrixMath.divideAllMatrixElements(semiDecryptedMessagePart, determinant);
            for (int j = 0; j < rowCount; j++) {
                numericalDecryptedMessage[indexOffset + j] = decryptedMessagePart.get(j)[0];
            }
        }

        return convertNumbersToMessage(numericalDecryptedMessage);
    }
}
