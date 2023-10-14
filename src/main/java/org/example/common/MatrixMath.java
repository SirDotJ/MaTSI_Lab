package org.example.common;

import java.util.ArrayList;
import java.util.List;

// Отвечает за алгоритмм выполнения операций над матрицами
// Используется int для избежания проблем с точностью при использовании вещественных чисел
public class MatrixMath {
	public static List<int[]> transposeMatrix(List<int[]> matrix) {
        List<int[]> transposedMatrix = new ArrayList<>();

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

    public static int matrixDeterminant (List<int[]> matrix) throws IllegalArgumentException {
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
            List<int[]> additionalMinor = additionalMinor(matrix, i, 0);
            double additionalMinorDeterminant = matrixDeterminant(additionalMinor);
            int sign = (int) Math.pow((-1), (i));

            determinantValue += (matrix.get(i)[0] * sign * additionalMinorDeterminant);
        }

        return determinantValue;
    }

    public static List<int[]> additionalMinor(List<int[]> matrix, int rowToErase, int columnToErase) throws IllegalArgumentException {
        int rowCount = matrix.size();
        int columnCount = matrix.get(0).length;

        if ((rowToErase < 0 || rowToErase >= rowCount) ||
            (columnToErase < 0 || columnToErase >= columnCount))
            throw new IllegalArgumentException("Invalid row and column indexes");

        List<int[]> additionalMinor = new ArrayList<>();
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

    public static List<int[]> attachedMatrix(List<int[]> matrix) throws IllegalArgumentException, ArithmeticException {
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
        List<int[]> attachedMatrix = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            attachedMatrix.add(new int[columnCount]);
            for (int j = 0; j < columnCount; j++) {
                List<int[]> additionalMinor = additionalMinor(matrix, i, j);
                int additionalMinorDeterminant = matrixDeterminant(additionalMinor);
                int sign = (int) Math.pow((-1), (i + j));

                attachedMatrix.get(i)[j] = sign * additionalMinorDeterminant;
            }
        }

        return attachedMatrix;
    }
    public static List<int[]> divideAllMatrixElements(List<int[]> matrix, int number) { // int Так как деление с double неточно для зашифровки
        List<int[]> quotient = new ArrayList<>(matrix);
        for (int[] row :
                quotient) {
            for (int i = 0; i < row.length; i++) {
                row[i] /= number;
            }
        }
        return quotient;
    }

    public static List<int[]> multiplyMatrices (List<int[]> multiplicand, List<int[]> multiplier) {
        int multiplicandRowCount = multiplicand.size();
        int multiplicandColumnCount = multiplicand.get(0).length;
        int multiplierRowCount = multiplier.size();
        int multiplierColumnCount = multiplier.get(0).length;

        if (multiplicandColumnCount != multiplierRowCount)
            throw new IllegalArgumentException("Provided matrices cannot be multiplied: column size is not equal to row size");

        List<int[]> product = new ArrayList<>();
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
