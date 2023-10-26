package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

public class RLE implements Encoder, Decoder {
    public static void main(String[] args) {
        String message = "ZZZZZECDAAAIIWWWWW";
        RLE encoder = new RLE();
        String encodedMesssage = encoder.encode(message);
        String decodedMessage = encoder.decode(encodedMesssage);
        System.out.println(message);
        System.out.println(encodedMesssage);
        System.out.println(decodedMessage);
    }

    /* Determines how many instances of letter in a row are present in message starting from first character */
    private static int countSameLettersInARow(String message, char letter) {
        int counter = 0;
        for (int i = 0; i < message.length(); i++) {
            char currentLetter = message.charAt(i);
            if (letter != currentLetter)
                break;
            counter++;
        }
        return counter;
    }

    private static String insertNumber(String originalMessage, int position, int number) {
        StringBuilder newMessage = new StringBuilder(originalMessage);
        newMessage.insert(position, number);
        return newMessage.toString();
    }

    /* Inserts numbers in front of a row of same/different characters according to RLE rules (positive: same letters in a row, negative: different letters in a row) */
    private static String markMessage(String plainMessage) {
        String fullMessage = String.copyValueOf(plainMessage.toCharArray());

        int differentLetterCounter = 0;
        int firstDifferentLetterIndex = -1;
        for (int i = 0; i < fullMessage.length(); i++) {
            char letter = fullMessage.charAt(i);

            String runningMessage = fullMessage.substring(i);
            int numberOfSameLetter = countSameLettersInARow(runningMessage, letter);
            if (numberOfSameLetter == 1) {
                differentLetterCounter++;
                if (firstDifferentLetterIndex == -1)
                    firstDifferentLetterIndex = i;
                continue;
            }

            if (differentLetterCounter > 1) {
                fullMessage = insertNumber(fullMessage, firstDifferentLetterIndex, -differentLetterCounter);
                i++;
            } else {
                fullMessage = insertNumber(fullMessage, i, numberOfSameLetter);
                i += numberOfSameLetter;
            }

            differentLetterCounter = 0;
            firstDifferentLetterIndex = -1;
        }

        return fullMessage;
    }

    /* Turns a marked message (see String markMessage(String)) into it's compressed form according to RLE rules */
    private static String compressMarkedMessage(String markedMessage) {
        StringBuilder compressedMessage = new StringBuilder(markedMessage);

        StringBuilder numberBuffer = new StringBuilder();
        for (int i = 0; i < compressedMessage.length(); i++) {
            char letter = compressedMessage.charAt(i);

            if (letter == '-' || Character.isDigit(letter)) {
                numberBuffer.append(letter);
                continue;
            }

            int number = Integer.parseInt(numberBuffer.toString());
            if (number < -1) {
                i += Math.abs(number) - 1;
            } else {
                compressedMessage.delete(i, i + number - 1);
            }

            numberBuffer = new StringBuilder();
        }

        return compressedMessage.toString();
    }

    /* Turns a compressed message (see String compressMarkedMessage(String)) into it's uncompressed form according to RLE rules */
    private static String decompressMessage(String compressedMessage) {
        StringBuilder decompressedMessage = new StringBuilder(compressedMessage);

        StringBuilder numberBuffer = new StringBuilder();
        for (int i = 0; i < decompressedMessage.length(); i++) {
            char letter = decompressedMessage.charAt(i);

            if (letter == '-' || Character.isDigit(letter)) {
                numberBuffer.append(letter);
                continue;
            }

            int number = Integer.parseInt(numberBuffer.toString());
            if (number < -1) {
                i += Math.abs(number) - 1;
            } else {
                decompressedMessage.insert(i, Character.toString(letter).repeat(number - 1));
                i += number - 1;
            }

            numberBuffer = new StringBuilder();
        }

        return decompressedMessage.toString();
    }

    /* Deletes RLE markings from provided marked message (see String markMessage(String)) */
    public static String cleanMessage(String markedMessage) {
        StringBuilder cleanMessage = new StringBuilder(markedMessage);
        for (int i = 0; i < cleanMessage.length(); i++) {
            char symbol = cleanMessage.charAt(i);
            if (Character.isDigit(symbol) || symbol == '-')
                cleanMessage.deleteCharAt(i--);
        }
        return cleanMessage.toString();
    }

    @Override
    public String encode(String plainMessage) {
        String markedMessage = markMessage(plainMessage);
        return compressMarkedMessage(markedMessage);
    }
    @Override
    public String decode(String compressedMessage) {
        String decompressedMessage = decompressMessage(compressedMessage);
        return cleanMessage(decompressedMessage);
    }
}
