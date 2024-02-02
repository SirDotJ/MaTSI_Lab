package org.togu.common;

import java.util.*;

// Класс хранит в себе предложенный алфавит в нижнем индексе с методами по поиску в нём
public class Alphabet {
    public enum TYPE {
		CYRILLIC,
		LATIN
	}
    private final List<Character> alphabet;
    private final TYPE type;

    public Alphabet(List<Character> alphabet, TYPE type) throws IllegalArgumentException {
        Set<Character> usedSymbols = new HashSet<>(alphabet);
        if (usedSymbols.size() != alphabet.size())
            throw new IllegalArgumentException("Passed alphabet is invalid: symbols are used more than once");

        this.alphabet = Collections.unmodifiableList(alphabet);
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
    @Override
    public String toString() {
        StringBuilder connectedAlphabet = new StringBuilder();
        for (Character letter : this.alphabet) {
            connectedAlphabet.append(letter);
        }
        return connectedAlphabet.toString();
    }

    public TYPE type() {
        return this.type;
    }

    public int size() {
        return this.alphabet.size();
    }
    public char get(int index) {
        return this.alphabet.get(index);
    }
    public String convert(List<Integer> indexes) {
        StringBuilder builder = new StringBuilder();
        for (int index : indexes)
            builder.append(this.get(index));
        return builder.toString();
    }
    public List<Integer> convert(String text) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            indexes.add(this.indexOf(letter));
        }
        return indexes;
    }
    public int indexOf(char letter) {
        return this.alphabet.indexOf(letter);
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
