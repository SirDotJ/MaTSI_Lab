package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

public class RLE implements Encoder, Decoder {
    public static void main(String[] args) {
        String message = "ZZZZZECDAAAIIWWWWW";
        RLE encoder = new RLE();
        String encodedMesssage = encoder.encode(message);
        System.out.println(message);
        System.out.println(encodedMesssage);
        System.out.println(cleanMessage(encodedMesssage));
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
        StringBuilder encodedMessage = new StringBuilder(plainMessage);

        int size = encodedMessage.length();
        int sameLetterCounter = 0;
        int firstSameLetterIndex = -1;
        for (int i = 0; i < size; i++) {
            StringBuilder encodedMessage =
            char letter = encodedMessage.charAt(i);
            String fullMessage = encodedMessage.toString();
            String runningMessage = encodedMessage.substring(i, size);

            int number = countSameLettersInARow(runningMessage, letter);
            if (number == 1) {
                sameLetterCounter++;
                if (firstSameLetterIndex == -1)
                    firstSameLetterIndex = i;
                continue;
            }

            if (sameLetterCounter > 1) {
                fullMessage = insertNumber(fullMessage, firstSameLetterIndex, -sameLetterCounter);
                size += Integer.toString(-sameLetterCounter).length();
                i++;
            }
            else {
                fullMessage = insertNumber(fullMessage, i, number);
                size += Integer.toString(sameLetterCounter).length();
                i += number;
            }


            sameLetterCounter = 0;
            firstSameLetterIndex = -1;

            encodedMessage = new StringBuilder(fullMessage);
        }

        return encodedMessage.toString();
    }

    // Returns copy of message with
    private static String markLetters(String message, int position) {

    }

    /* Turns a marked message (see String markMessage(String)) into it's compressed form according to RLE rules */
    private static String compressMarkedMessage(String markedMessage) {
        return null;
    }

    /* Turns a compressed message (see String compressMarkedMessage(String)) into it's uncompressed form according to RLE rules */
    private static String decompressMessage(String compressedMessage) {
        return null;
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
