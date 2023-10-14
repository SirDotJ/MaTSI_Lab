package org.example.common;

public class AlphabetConstants {
	final static public char SPACE_CHARACTER = '_';
	final static public Alphabet CYRILLIC_NO_SPACE = new Alphabet(new char[] {
		'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
	});
	final static public Alphabet CYRILLIC_WITH_SPACE = new Alphabet(new char[] {
		'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', SPACE_CHARACTER
	});
	final static public Alphabet LATIN_NO_SPACE = new Alphabet(new char[] {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
	});
	final static public Alphabet LATIN_WITH_SPACE = new Alphabet(new char[] {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', SPACE_CHARACTER
	});
	final static public Alphabet FULL_WITH_SPACE = new Alphabet(LATIN_NO_SPACE.toString() + CYRILLIC_WITH_SPACE.toString());
	final static public Alphabet FULL_NO_SPACE = new Alphabet(LATIN_NO_SPACE.toString() + CYRILLIC_NO_SPACE.toString());
}
