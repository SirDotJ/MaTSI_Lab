package org.example.lab5;

import org.example.common.Decoder;
import org.example.common.Encoder;

public class RLE implements Encoder, Decoder {
    public static void main(String[] args) {
        String message = "ZZZZZECDAAAIIWWWWW";
        RLE encoder = new RLE();
        String encodedMesssage = encoder.encode(message);
        System.out.println(encodedMesssage);
    }

    @Override
    public String encode(String plainMessage) {
        StringBuilder encodedMessage = new StringBuilder(plainMessage);

        int size = encodedMessage.length();
        int sameLetterCounter = 0;
        int firstSameLetterIndex = -1;
        for (int i = 0; i < size; i++) {
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

    @Override
    public String decode(String encodedMessage) {
        StringBuilder decodedMessage = new StringBuilder(encodedMessage);

        return decodedMessage.toString();
    }

    private String insertNumber(String originalMessage, int position, int number) {
        StringBuilder newMessage = new StringBuilder(originalMessage);
        newMessage.insert(position, number);
        return newMessage.toString();
    }
    private int countSameLettersInARow(String message, char letter) {
        int counter = 0;
        for (int i = 0; i < message.length(); i++) {
            char currentLetter = message.charAt(i);
            if (letter != currentLetter)
                break;
            counter++;
        }
        return counter;
    }
}
