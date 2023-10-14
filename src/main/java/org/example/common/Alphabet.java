package org.example.common;

import java.util.*;

// Класс хранит в себе предложенный алфавит в нижнем индексе с методами по поиску в нём
public class Alphabet {
    private final static boolean DEFAULT_LOWER_CASE = true;
    private final List<Character> alphabet;
    public Alphabet(List<Character> alphabet) throws IllegalArgumentException {
        Set<Character> usedSymbols = new HashSet<>(alphabet);
        if (usedSymbols.size() != alphabet.size())
            throw new IllegalArgumentException("Passed alphabet is invalid: symbols are used more than once");

        List<Character> lowerCaseFormAlphabet = new ArrayList<>();
        for (char originalLetter : alphabet) {
            char lowerCaseLetter = Character.toLowerCase(originalLetter);
            lowerCaseFormAlphabet.add(lowerCaseLetter);
        }
        this.alphabet = Collections.unmodifiableList(lowerCaseFormAlphabet);
    }
    public Alphabet(Character[] alphabet) {
        this(Arrays.asList(alphabet));
    }
    public Alphabet(char[] alphabet) {
        this(new String(alphabet));
    }
    public Alphabet(String alphabet) {
        this(alphabet.chars().mapToObj(c -> (char) c).toArray(Character[]::new));
    }
    public String toString(boolean inLowerCase) {
        StringBuilder connectedAlphabet = new StringBuilder();
        for (Character letter : this.alphabet) {
            char outputLetter = inLowerCase ? Character.toLowerCase(letter)
                                            : Character.toUpperCase(letter);
            connectedAlphabet.append(outputLetter);
        }
        return connectedAlphabet.toString();
    }

    @Override
    public String toString() {
        return this.toString(DEFAULT_LOWER_CASE);
    }

    public int size() {
        return this.alphabet.size();
    }
    public char get(int index, boolean lowerCase) {
        char letter = lowerCase ? Character.toLowerCase(this.alphabet.get(index))
                                : Character.toUpperCase(this.alphabet.get(index));
        return letter;
    }
    public char get(int index) {
        return this.get(index, DEFAULT_LOWER_CASE);
    }
    public int indexOf(char letter) {
        char lowerCaseLetter = Character.toLowerCase(letter);
        return this.alphabet.indexOf(lowerCaseLetter);
    }
    public boolean letterInAlphabet(char letter) {
        return this.alphabet.contains(letter);
    }
    public boolean textInAlphabet(char[] text) {
        for (char c : text) {
            if (!this.letterInAlphabet(c))
                return false;
        }
        return true;
    }
    public boolean textInAlphabet(String text) {
        return this.textInAlphabet(text.toCharArray());
    }
}
