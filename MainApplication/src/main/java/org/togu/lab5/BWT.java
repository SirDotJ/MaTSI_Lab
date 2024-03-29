package org.togu.lab5;

import org.togu.common.Decoder;
import org.togu.common.Encoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BWT implements Encoder, Decoder {
    /* Данный символ используется для обозначения окончания слова для его корректного нахождения при раскодировании */
    private static final char SPECIAL_END_CHARACTER = 'ѐ'; // Данный символ выбран так как он по своему коду находится далеко от букв кириллицы и латинского алфавита

    public static void main(String[] args) {
        String message = "BANANAS";
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

    private static String getPlainTextColumn(List<String> symbolMatrix) throws IllegalArgumentException {
        for (String row :
                symbolMatrix) {
            if (row.charAt(row.length() - 1) == SPECIAL_END_CHARACTER)
                return row;
        }
        throw new IllegalArgumentException("Provided matrix does not have a row string with special character: " + SPECIAL_END_CHARACTER + " at the end");
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

    private static String cleanMessage(String message, char letterToDelete) {
        StringBuilder cleanedMessage = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char letter = message.charAt(i);
            if (letter == letterToDelete)
                continue;
            cleanedMessage.append(letter);
        }
        return cleanedMessage.toString();
    }

    @Override
    public String encode(String plainMessage) {
        String messageCopy = plainMessage;
        if (messageCopy.charAt(messageCopy.length() - 1) != SPECIAL_END_CHARACTER)
            messageCopy = messageCopy + SPECIAL_END_CHARACTER;

        List<String> rotations = getSortedRotations(messageCopy);
        return getColumn(rotations, rotations.size() - 1);
    }

    @Override
    public String decode(String encodedMessage) {
        String messageCopy = encodedMessage;
        if (messageCopy.charAt(messageCopy.length() - 1) != SPECIAL_END_CHARACTER)
            messageCopy = messageCopy + SPECIAL_END_CHARACTER;

        List<String> reconstructedRotations = reconstructRotations(messageCopy);
        String decodedColumn = getPlainTextColumn(reconstructedRotations);
        return cleanMessage(decodedColumn, SPECIAL_END_CHARACTER);
    }
}
