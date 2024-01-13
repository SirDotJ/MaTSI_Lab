package org.example.lab4;

public class Bigramm {
    private final char firstLetter;
    private final char secondLetter;
    public Bigramm(char firstLetter, char secondLetter) {
        this.firstLetter = firstLetter;
        this.secondLetter = secondLetter;
    }
    public char getFirstLetter() {
        return firstLetter;
    }
    public char getSecondLetter() {
        return secondLetter;
    }
    @Override
    public String toString() {
        return Character.toString(this.firstLetter) + Character.toString(this.secondLetter);
    }
}
