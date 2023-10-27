package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BWT implements Encoder, Decoder {

    public static void main(String[] args) {
        String message = "AJJAJAJIIIAKKAKAKKAJJIIAJJKAJJKABKAIBBBBL";
        BWT encoder = new BWT();
        RLE compressor = new RLE();
        String encodedMessage = encoder.encode(message);
        String decodedMessage = encoder.decode(encodedMessage);
        System.out.println("Message: " + message);
        System.out.println("BWT Encoded Form: " + encodedMessage);
        System.out.println("BWT Decoded Form: " + decodedMessage + "\n");

        String rleNoBWT = compressor.encode(message);
        String rleYesBWT = compressor.encode(encodedMessage);
        System.out.println("Comparison using RLE:");
        System.out.println("For message without BWT: " + rleNoBWT);
        System.out.println("For message with BWT: " + rleYesBWT);
        System.out.println("Difference: " + (rleNoBWT.length() - rleYesBWT.length()));
        System.out.println("Benefit without BWT: " + (message.length() - rleNoBWT.length()));
        System.out.println("Benefit with BWT: " + (message.length() - rleYesBWT.length()));
    }

    private static List<String> reconstructRotations(String column) {
        List<String> rotations = new ArrayList<>();

        for (int i = 0; i < column.length(); i++) {
            insertColumn(rotations, column);
            sortList(rotations);
        }

        return rotations;
    }

    private static void insertColumn(List<String> destination, String column) {
        if (destination.isEmpty()) {
            for (int i = 0; i < column.length(); i++)
                destination.add(String.valueOf(column.charAt(i)));
            return;
        }

        insertColumn(destination, column, destination.get(0).length() - 1);
    }

    private static void insertColumn(List<String> destination, String column, int index) {
        if (destination == null || destination.isEmpty())
            throw new IllegalArgumentException("Provided destination is empty or is not initialized");

        if (Math.abs(index) >= destination.size())
            throw new IllegalArgumentException("Provided index is out of range");

        if (column.length() != destination.size())
            throw new IllegalArgumentException("Provided column's size is not equal to destination size");

        for (int i = 0; i < destination.size(); i++) {
            destination.set(i, column.charAt(i) + destination.get(i));
        }
    }

    private static void sortList(List<String> stringList) {
        Collections.sort(stringList);
    }

    private static String getColumn(List<String> symbolMatrix, int columnIndex) {
        StringBuilder column = new StringBuilder();
        for (String row : symbolMatrix) {
            column.append(row.charAt(columnIndex));
        }
        return column.toString();
    }

    private static String getRow(List<String> symbolMatrix, int rowIndex) {
        return symbolMatrix.get(rowIndex);
    }

    private static List<String> getSortedRotations(String text) {
        List<String> rotations = getAllTextRotations(text);
        sortList(rotations);
        return rotations;
    }

    /* Возвращает матрицу всех возможных сдвигов предоставленного текста */
    private static List<String> getAllTextRotations(String text) {
        List<String> rotations = new ArrayList<>();
        for (int i = 0; i < text.length(); i++)
            rotations.add(rotateText(text, i));
        return rotations;
    }

    /* Возвращает копию текста, все символы которого были сдвинуты на offset позиций вправо */
    private static String rotateText(String text, int offset) {
        StringBuilder rotatedText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int newLetterForIndex = i - offset;
            newLetterForIndex = newLetterForIndex < 0
                    ? text.length() - Math.abs(newLetterForIndex)
                    : newLetterForIndex;
            rotatedText.append(text.charAt(newLetterForIndex));
        }
        return rotatedText.toString();
    }
    @Override
    public String encode(String plainMessage) {
        List<String> rotations = getSortedRotations(plainMessage);
        return getColumn(rotations, rotations.size() - 1);
    }

    @Override
    public String decode(String encodedMessage) {
        List<String> reconstructedRotations = reconstructRotations(encodedMessage);
        return getRow(reconstructedRotations, 0);
    }
}
