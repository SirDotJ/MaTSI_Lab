package org.example.lab3;

import org.example.common.Encryptor;
import org.example.common.GlobalVariables;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MatrixMultiplicationEncryptor implements Encryptor {
	public static int sizeMatrix = 3;
	private static int lengthAlphabet = GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.size();
	private double[][] key;
	public MatrixMultiplicationEncryptor(String key) {
		String[] rows = key.split("\n");
		String[] exampleRow = rows[0].split(" ");
		this.key = new double[rows.length][exampleRow.length];
		for (int i = 0; i < rows.length; i++) {
			String[] row = rows[i].split(" ");
			for (int j = 0; j < row.length; j++) {
				this.key[i][j] = Double.parseDouble(row[j]);
			}
		}
	}

    private static double[][] transposeMatrix(double[][] matrix){
        double[][] temp = new double[sizeMatrix][sizeMatrix];
        for (int i = 0; i < sizeMatrix; i++)
            for (int j = 0; j < sizeMatrix; j++)
                temp[j][i] = matrix[i][j];
        return temp;
    }

    private static double determinantMatrixForElementsInverse (double[][] matrix, int i, int j) {
        double determinantMatrixForElementsInverse;
        double[] matrixForElementsInverse = new double[(sizeMatrix - 1) * (sizeMatrix - 1)]; //вспомогательная матрица
        int count = 0;
        for (int m = 0; m < sizeMatrix; m++) {
            for (int n = 0; n < sizeMatrix; n++) {
                if (m == i || n == j)
                    continue;
                matrixForElementsInverse[count] = matrix[m][n];
                count++;
            }
        }

        determinantMatrixForElementsInverse = matrixForElementsInverse[0] * matrixForElementsInverse[3] -
                matrixForElementsInverse[1] * matrixForElementsInverse[2];

        return determinantMatrixForElementsInverse;
    }

    private static double[][] inverseMatrix (double[][] matrix) {
        double[][] matrixInverse = new double[sizeMatrix][sizeMatrix];
        double determinant = matrix[0][0] * matrix[1][1] * matrix[2][2] +
                matrix[0][2] * matrix[1][0] * matrix[2][1] + matrix[0][1] * matrix[1][2] * matrix[2][0] -
                (matrix[0][2] * matrix[1][1] * matrix[2][0] + matrix[0][1] * matrix[1][0] * matrix[2][2] +
                        matrix[0][0] * matrix[1][2] * matrix[2][1]);

        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++) {
                matrixInverse[i][j] = (Math.pow((-1), (i + j)) * determinantMatrixForElementsInverse(matrix, i, j))/determinant;
            }
        }

        return matrixInverse;
    }

    public String encrypt (String text) {
        int[] numberText;
		double[][] matrix = this.key.clone();
        int textLength = text.length();
        String resultEncryption = new String();

        //определение кратности строки на 3 символа
        while(textLength%sizeMatrix != 0) {
            text += " ";
            textLength++;
        }
        numberText = new int[textLength];
        int[] resultMatrix = new int[textLength];
        //преобразование символов в порядковые номера
        for (int i = 0; i < textLength; i++) {
            if (text.charAt(i) == ' ') {
                numberText[i] = 0;
            }
            else {
                for (int j = 0; j < lengthAlphabet; j++) {
                    if (text.charAt(i) == GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(j)) {
                        numberText[i] = j + 1;
                    }
                }
            }

        }

        for (int i = 0; i < textLength / sizeMatrix; i++){
            for (int j = 0; j < sizeMatrix; j++) {
                int tempNumber = 0;
                for (int m = 0; m < sizeMatrix; m++) {
                    tempNumber += matrix[j][m] * numberText[sizeMatrix*i + m];
                }
                resultMatrix[sizeMatrix * i + j] = tempNumber;
            }
        }

        for (int i = 0; i < textLength; i++) {
            resultEncryption += resultMatrix[i] + " ";
        }
        return resultEncryption;
    }

    public String decrypt (String text) {
		double[][] matrix = this.key.clone();
        String[] strArray = text.split(" ");
        int[] numberText = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            numberText[i] = Integer.parseInt(strArray[i]);
        }
        String resultDecryption = new String();
        matrix = transposeMatrix(matrix);
        double[][] matrixInverse = inverseMatrix(matrix);
        int[] resultMatrix = new int[strArray.length];

        for (int i = 0; i < strArray.length / sizeMatrix; i++){
            for (int j = 0; j < sizeMatrix; j++) {
                double tempNumber = 0;
                for (int m = 0; m < sizeMatrix; m++) {
                    tempNumber += matrixInverse[j][m] * numberText[sizeMatrix * i + m];
                }
                resultMatrix[sizeMatrix * i + j] = (int)tempNumber;
            }
        }

        for(int i = 0; i < strArray.length; i++) {
            for(int j = 0; j < lengthAlphabet; j++) {
                if(resultMatrix[i] == 0)
                    break;
                else {
                    resultDecryption += GlobalVariables.CYRILLIC_ALPHABET_UPPERCASE.get(resultMatrix[i] - 1);
                    break;
                }
            }
        }
        return resultDecryption;
    }
}
