package org.example.common;

import java.lang.reflect.Array;
import java.util.*;

// Класс хранит в себе предложенный алфавит в нижнем индексе с методами по поиску в нём
public class Alphabet {
    public enum TYPE {
		CYRILLIC,
		LATIN
	}
    private final static boolean DEFAULT_LOWER_CASE = true;
    private final List<Character> alphabet;
    private final TYPE type;

//    public static void main(String[] args) {
//        Alphabet alphabet1 = AlphabetConstants.CYRILLIC_NO_SPACE;
//
//    }

    public Alphabet(List<Character> alphabet, TYPE type) throws IllegalArgumentException {
        Set<Character> usedSymbols = new HashSet<>(alphabet);
        if (usedSymbols.size() != alphabet.size())
            throw new IllegalArgumentException("Passed alphabet is invalid: symbols are used more than once");

        List<Character> lowerCaseFormAlphabet = new ArrayList<>();
        for (char originalLetter : alphabet) {
            char lowerCaseLetter = Character.toLowerCase(originalLetter);
            lowerCaseFormAlphabet.add(lowerCaseLetter);
        }
        this.alphabet = Collections.unmodifiableList(lowerCaseFormAlphabet);
        this.type = type;
    }
    public Alphabet(Character[] alphabet, TYPE type) {
        this(Arrays.asList(alphabet), type);
    }
    public Alphabet(char[] alphabet, TYPE type) {
        this(new String(alphabet), type);
    }
    public Alphabet(String alphabet, TYPE type) {
        this(alphabet.chars().mapToObj(c -> (char) c).toArray(Character[]::new), type);
    }
    public Alphabet(List<Character> alphabet) {
        this(alphabet, determineType(alphabet));
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

    public TYPE type() {
        return this.type;
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

    private static TYPE determineType(List<Character> alphabet) {
        return Character.UnicodeBlock.of(alphabet.get(0)).equals(Character.UnicodeBlock.BASIC_LATIN) ? TYPE.LATIN : TYPE.CYRILLIC;
    }
}
