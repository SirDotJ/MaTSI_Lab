package org.example.lab3;

import org.example.common.Encryptor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MatrixMultiplicationEncryptor implements Encryptor {
	public static int sizeMatrix = 3;
    final static public ArrayList<Character> CYRILLIC_ALPHABET_UPPERCASE = new ArrayList<>(Arrays.asList(
			'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', '_'
	));
	private static int lengthAlphabet = CYRILLIC_ALPHABET_UPPERCASE.size();
	private ArrayList<ArrayList<Double>> key;
	public MatrixMultiplicationEncryptor(String key) {
		this.key = new ArrayList<>();
		String[] rows = key.split("\n");
		ArrayList<String> data = new ArrayList<>();
		for (int i = 0; i < rows.length; i++) {
			String[] numbers = rows[i].split(" ");
			ArrayList<Double> rowNumbers = new ArrayList<>();
			for (int j = 0; j < data.size(); j++) {
				rowNumbers.add(Double.parseDouble(numbers[j]));
			}
			this.key.add(rowNumbers);
		}
	}

    private static ArrayList<ArrayList<Double>> transposeMatrix(ArrayList<ArrayList<Double>> matrix){
        ArrayList<ArrayList<Double>> temp = new ArrayList<>(sizeMatrix);
		for (int i = 0; i < temp.size(); i++) {
			temp.set(i, new ArrayList<>(sizeMatrix));
		}

        for (int i = 0; i < sizeMatrix; i++)
            for (int j = 0; j < sizeMatrix; j++)
                temp.get(j).set(i, matrix.get(i).get(j));

        return temp;
    }

    private static double determinantMatrixForElementsInverse (ArrayList<ArrayList<Double>> matrix, int i, int j) {
        double determinantMatrixForElementsInverse;
        double[] matrixForElementsInverse = new double[(sizeMatrix - 1) * (sizeMatrix - 1)]; //вспомогательная матрица
        int count = 0;
        for (int m = 0; m < sizeMatrix; m++) {
            for (int n = 0; n < sizeMatrix; n++) {
                if (m == i || n == j)
                    continue;
                matrixForElementsInverse[count] = matrix.get(m).get(n);
                count++;
            }
        }

        determinantMatrixForElementsInverse = matrixForElementsInverse[0] * matrixForElementsInverse[3] -
                matrixForElementsInverse[1] * matrixForElementsInverse[2];

        return determinantMatrixForElementsInverse;
    }

    private static double[][] inverseMatrix (ArrayList<ArrayList<Double>> matrix) {
        double[][] matrixInverse = new double[sizeMatrix][sizeMatrix];
        double determinant = matrix.get(0).get(0) * matrix.get(1).get(1) * matrix.get(2).get(2) +
                matrix.get(0).get(2) * matrix.get(1).get(0) * matrix.get(2).get(1) + matrix.get(0).get(1) * matrix.get(1).get(2) * matrix.get(2).get(0)-
                (matrix.get(0).get(2) * matrix.get(1).get(1) * matrix.get(2).get(0) + matrix.get(0).get(1) * matrix.get(1).get(0) * matrix.get(2).get(2) +
                        matrix.get(0).get(0) * matrix.get(1).get(2) * matrix.get(2).get(1));

        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++) {
                matrixInverse[i][j] = (Math.pow((-1), (i + j)) * determinantMatrixForElementsInverse(matrix, i, j))/determinant;
            }
        }

        return matrixInverse;
    }
	@Override
	public String encrypt(String message) {
		int[] numberText;
        int textLength = message.length();
        String resultEncryption = new String();

        //определение кратности строки на 3 символа
        while(textLength%sizeMatrix != 0) {
            message += " ";
            textLength++;
        }
        numberText = new int[textLength];
        int[] resultMatrix = new int[textLength];
        //преобразование символов в порядковые номера
        for (int i = 0; i < textLength; i++) {
            if (message.charAt(i) == ' ') {
                numberText[i] = 0;
            }
            else {
                for (int j = 0; j < lengthAlphabet; j++) {
                    if (message.charAt(i) == CYRILLIC_ALPHABET_UPPERCASE.get(j)) {
                        numberText[i] = j + 1;
                    }
                }
            }

        }

        for (int i = 0; i < textLength / sizeMatrix; i++){
            for (int j = 0; j < sizeMatrix; j++) {
                int tempNumber = 0;
                for (int m = 0; m < sizeMatrix; m++) {
                    tempNumber += this.key.get(j).get(m) * numberText[sizeMatrix*i + m];
                }
                resultMatrix[sizeMatrix * i + j] = tempNumber;
            }
        }

        for (int i = 0; i < textLength; i++) {
            resultEncryption += resultMatrix[i] + " ";
        }
        return resultEncryption;
	}

	@Override
	public String decrypt(String message) {
		String[] strArray = message.split(" ");
        int[] numberText = new int[strArray.length];
        for(int i = 0; i < strArray.length; i++) {
            numberText[i] = Integer.parseInt(strArray[i]);
        }
        String resultDecryption = new String();
        this.key = transposeMatrix(this.key);
        double[][] matrixInverse = inverseMatrix(this.key);
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
                    resultDecryption += CYRILLIC_ALPHABET_UPPERCASE.get(resultMatrix[i] - 1);
                    break;
                }
            }
        }
        return resultDecryption;
	}
}
