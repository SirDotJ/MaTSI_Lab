package org.example.lab3;

import org.example.common.Encryptor;
import org.example.common.GlobalVariables;

import java.util.ArrayList;

public class MatrixMultiplicationEncryptor implements Encryptor {
	public static int sizeMatrix = 3;
	private static final int lengthAlphabet = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.size();
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

    public String encrypt (String text) {
        StringBuilder builder = new StringBuilder(text);
//        if(text.length() % this.key.length != 0) {
//            int difference = text.length() % this.key.length;
//            for (int i = 0; i < difference - 1; i++) {
//                builder.append("_");
//            }
//        }
//        text = builder.toString();
//        int[] numberText;
//		ArrayList<double[]> matrix = (ArrayList<double[]>) this.key.clone();
//        int textLength = text.length();
//        String resultEncryption = new String();
//
//        //определение кратности строки на 3 символа
//        while(textLength%sizeMatrix != 0) {
//            text += " ";
//            textLength++;
//        }
//        numberText = new int[textLength];
//        int[] resultMatrix = new int[textLength];
//        //преобразование символов в порядковые номера
//        for (int i = 0; i < textLength; i++) {
//            if (text.charAt(i) == ' ') {
//                numberText[i] = 0;
//            }
//            else {
//                for (int j = 0; j < lengthAlphabet; j++) {
//                    if (text.charAt(i) == GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(j)) {
//                        numberText[i] = j + 1;
//                    }
//                }
//            }
//
//        }
//
//        for (int i = 0; i < textLength / sizeMatrix; i++){
//            for (int j = 0; j < sizeMatrix; j++) {
//                int tempNumber = 0;
//                for (int m = 0; m < sizeMatrix; m++) {
//                    tempNumber += matrix[j][m] * numberText[sizeMatrix*i + m];
//                }
//                resultMatrix[sizeMatrix * i + j] = tempNumber;
//            }
//        }
//
//        for (int i = 0; i < textLength; i++) {
//            resultEncryption += resultMatrix[i] + " ";
//        }
        return "resultEncryption";
    }

    // works but waits for rework
    public String decrypt (String text) {
//		ArrayList<double[]> decryptionKey = (ArrayList<double[]>) this.key.clone();
//        String[] strArray = text.split(" ");
//        int[] numberText = new int[strArray.length];
//        for(int i = 0; i < strArray.length; i++) {
//            numberText[i] = Integer.parseInt(strArray[i]);
//        }
//        String resultDecryption = new String();
//        matrix = this.transposedKey();
//        ArrayList<double[]> matrixInverse = inverseMatrix(matrix);
//        int[] resultMatrix = new int[strArray.length];
//
//        for (int i = 0; i < strArray.length / sizeMatrix; i++){
//            for (int j = 0; j < sizeMatrix; j++) {
//                double tempNumber = 0;
//                for (int m = 0; m < sizeMatrix; m++) {
//                    tempNumber += matrixInverse[j][m] * numberText[sizeMatrix * i + m];
//                }
//                resultMatrix[sizeMatrix * i + j] = (int)tempNumber;
//            }
//        }
//
//        for(int i = 0; i < strArray.length; i++) {
//            for(int j = 0; j < lengthAlphabet; j++) {
//                if(resultMatrix[i] == 0)
//                    break;
//                else {
//                    resultDecryption += GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(resultMatrix[i] - 1);
//                    break;
//                }
//            }
//        }
        return "resultDecryption";
    }
}
