package org.example.common;

import java.util.ArrayList;
import java.util.Arrays;

// Отвечает за алгоритмм выполнения операций над матрицами
public class MatrixMath {
    static final ArrayList<int[]> TEST_MATRIX1 = new ArrayList<>(Arrays.asList(
            new int[] {1, 2, 3},
            new int[] {4, 5, 6},
            new int[] {7, 8, 9},
            new int[] {10, 11, 12}
    ));
    static final ArrayList<int[]> TEST_MATRIX2 = new ArrayList<>(Arrays.asList(
            new int[] {1, 2, 3, 4, 5},
            new int[] {6, 7, 8, 9, 10},
            new int[] {11, 12, 13, 14, 15}
    ));
    static final ArrayList<int[]> TEST_SQUARE_MATRIX = new ArrayList<>(Arrays.asList(
            new int[] {1, 2, 6, 5},
            new int[] {11, 5, 6, 16},
            new int[] {7, 8, 9, 3},
            new int[] {2, 24, 2, 34}
    ));
    public static void main(String[] args) {
        System.out.println("Квадратная матрица:");
        printMatrix(TEST_SQUARE_MATRIX);
        System.out.println("\nОпределитель матрицы:");
        System.out.println(matrixDeterminant(TEST_SQUARE_MATRIX));
        System.out.println("\nТранспонированная матрица");
        printMatrix(transposeMatrix(TEST_SQUARE_MATRIX));
        try {
            System.out.println("\nОбратная матрица:");
            printMatrix(attachedMatrix(TEST_SQUARE_MATRIX));
        } catch (ArithmeticException e) {
            System.out.println("Матрица не имеет обратной матрицы, так как определитель равен 0");
        }
        System.out.println("Матрица №1:");
        printMatrix(TEST_MATRIX1);
        System.out.println("Матрица №2");
        printMatrix(TEST_MATRIX2);
        System.out.println("Умножение двух матриц выше:");
        printMatrix(multiplyMatrices(TEST_MATRIX1, TEST_MATRIX2));
    }
    public static void printMatrix(ArrayList<int[]> matrix) {
        if(matrix == null) {
            System.out.println("Переданной матрицы нет");
            return;
        } else if (matrix.isEmpty()) {
            System.out.println("Переданная матрица: пустая");
            return;
        }

        for (int[] row :
                matrix) {
            if (row.length == 0) {
                System.out.println("Пустая строка");
                continue;
            }

            System.out.print("| " + row[0]);
            for (int i = 1; i < row.length; i++) {
                System.out.print(" | " + row[i]);
            }

            System.out.println(" |");
        }
    }
	public static ArrayList<int[]> transposeMatrix(ArrayList<int[]> matrix) {
        ArrayList<int[]> transposedMatrix = new ArrayList<>();

        int rowCount = matrix.size();
        int columnCount = matrix.get(0).length;

        for (int i = 0; i < columnCount; i++) {
            transposedMatrix.add(new int[rowCount]);
        }

        for (int i = 0; i < rowCount; i++)
            for (int j = 0; j < columnCount; j++)
                transposedMatrix.get(j)[i] = matrix.get(i)[j];

        return transposedMatrix;
    }

    public static int matrixDeterminant (ArrayList<int[]> matrix) throws IllegalArgumentException {
        if(matrix.size() != matrix.get(0).length)
            throw new IllegalArgumentException("Passed matrix is not a square matrix");

        if (matrix.size() == 1) {
            return matrix.get(0)[0];
        }

        if (matrix.size() == 2) {
            return (matrix.get(0)[0] * matrix.get(1)[1] - matrix.get(0)[1] * matrix.get(1)[0]);
        }

        int matrixSize = matrix.size();

        int determinantValue = 0;
        for (int i = 0; i < matrixSize; i++) {
            ArrayList<int[]> additionalMinor = additionalMinor(matrix, i, 0);
            double additionalMinorDeterminant = matrixDeterminant(additionalMinor);
            int sign = (int) Math.pow((-1), (i));

            determinantValue += matrix.get(i)[0] * sign * additionalMinorDeterminant;
        }

        return determinantValue;
    }

    public static ArrayList<int[]> additionalMinor(ArrayList<int[]> matrix, int rowToErase, int columnToErase) throws IllegalArgumentException {
        int rowCount = matrix.size();
        int columnCount = matrix.get(0).length;

        if ((rowToErase < 0 || rowToErase >= rowCount) ||
            (columnToErase < 0 || columnToErase >= columnCount))
            throw new IllegalArgumentException("Invalid row and column indexes");

        ArrayList<int[]> additionalMinor = new ArrayList<>();
        int minorRowCount = rowCount - 1;
        int minorRowIndex = -1;
        for (int i = 0; i < rowCount; i++) {
            if (i == rowToErase)
                continue;
            additionalMinor.add(new int[minorRowCount]);
            minorRowIndex++;

            int minorColumnIndex = 0;
            for (int j = 0; j < columnCount; j++) {
                if (j == columnToErase)
                    continue;
                additionalMinor.get(minorRowIndex)[minorColumnIndex++] = matrix.get(i)[j];
            }
        }

        return additionalMinor;
    }

    public static ArrayList<int[]> attachedMatrix(ArrayList<int[]> matrix) throws IllegalArgumentException, ArithmeticException {
        int rowCount = matrix.size();
        int columnCount = matrix.get(0).length;

        // Определитель нельзя вычислить для неквадратной матрицы
        if (rowCount != columnCount)
            throw new IllegalArgumentException("Matrix is not a square matrix");

        // Не существует обратной матрицы для матрицы с определителем равным нулю
        double matrixDeterminant = matrixDeterminant(matrix);
        if (matrixDeterminant == 0)
            throw new ArithmeticException("Inverse matrix cannot be found for matrix with determinant equal to 0");

        // Нахождение присоединенной матрицы
        ArrayList<int[]> attachedMatrix = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            attachedMatrix.add(new int[columnCount]);
            for (int j = 0; j < columnCount; j++) {
                ArrayList<int[]> additionalMinor = additionalMinor(matrix, i, j);
                int additionalMinorDeterminant = matrixDeterminant(additionalMinor);
                int sign = (int) Math.pow((-1), (i + j));

                attachedMatrix.get(i)[j] = sign * additionalMinorDeterminant;
            }
        }

        return attachedMatrix;
    }
    public static ArrayList<int[]> divideAllMatrixElements(ArrayList<int[]> matrix, int number) { // int Так как деление с double неточно для зашифровки
        ArrayList<int[]> quotient = (ArrayList<int[]>) matrix.clone();
        for (int[] row :
                quotient) {
            for (int i = 0; i < row.length; i++) {
                row[i] /= number;
            }
        }
        return quotient;
    }

    public static ArrayList<int[]> multiplyMatrices (ArrayList<int[]> multiplicand, ArrayList<int[]> multiplier) {
        int multiplicandRowCount = multiplicand.size();
        int multiplicandColumnCount = multiplicand.get(0).length;
        int multiplierRowCount = multiplier.size();
        int multiplierColumnCount = multiplier.get(0).length;

        if (multiplicandColumnCount != multiplierRowCount)
            throw new IllegalArgumentException("Provided matrices cannot be multiplied: column size is not equal to row size");

        ArrayList<int[]> product = new ArrayList<>();
        for (int i = 0; i < multiplicandRowCount; i++) {
            product.add(new int[multiplierColumnCount]);
            for (int j = 0; j < multiplierColumnCount; j++) {
                int sum = 0;
                for (int k = 0; k < multiplicandColumnCount; k++) {
                    sum += multiplicand.get(i)[k] * multiplier.get(k)[j];
                }
                product.get(i)[j] = sum;
            }
        }

        return product;
    }
}
