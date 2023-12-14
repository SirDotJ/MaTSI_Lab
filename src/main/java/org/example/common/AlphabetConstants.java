package org.example.common;

public class AlphabetConstants {
	final static public char SPACE_CHARACTER = '_';
	final static public Alphabet CYRILLIC_NO_SPACE = new Alphabet(new char[] {
		'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
	});
	final static public Alphabet CYRILLIC_WITH_SPACE = new Alphabet(new char[] {
		'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', SPACE_CHARACTER
	});
	final static public Alphabet CYRILLIC_UPPER_CASE = new Alphabet(new char[] {
		'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'
	});
	final static public Alphabet LATIN_NO_SPACE = new Alphabet(new char[] {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
	});
	final static public Alphabet LATIN_WITH_SPACE = new Alphabet(new char[] {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', SPACE_CHARACTER
	});
	final static public Alphabet LATIN_UPPER_CASE = new Alphabet(new char[] {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
	});
	final static public Alphabet NUMBERS = new Alphabet(new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
	});
	final static public Alphabet OTHER_KEYBOARD_KEYS = new Alphabet(new char[] {
		' ', '~', '`', '!', '@', '\"', '\'', '#', '№', '$', ';', '%', ':', '^', '&', '?', '*', '(', ')', '-', '+', '=', '[', ']', '{', '}', '\\', '/', '.', '<', '>', '\t', '\b', '\n', '\r', '\f'
	});
	final static public Alphabet FULL_WITH_SPACE = new Alphabet(LATIN_NO_SPACE.toString() + CYRILLIC_WITH_SPACE.toString());
	final static public Alphabet FULL_NO_SPACE = new Alphabet(LATIN_NO_SPACE.toString() + CYRILLIC_NO_SPACE.toString());
	public static final Alphabet FULL_KEYBOARD = new Alphabet(LATIN_WITH_SPACE.toString() + LATIN_UPPER_CASE.toString() + CYRILLIC_NO_SPACE.toString() + CYRILLIC_UPPER_CASE.toString() + NUMBERS.toString() + OTHER_KEYBOARD_KEYS.toString());
}
