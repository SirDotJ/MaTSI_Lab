package org.togu.lab5;

import java.util.*;

public class BinaryWordTree {
	private static final String DEFAULT_SOURCE_MESSAGE = "test";
	private List<LetterNode> letters;
	private HashMap<Character, List<Integer>> paths;
	private LetterNode root;

	public BinaryWordTree(String message) {
		this.letters =  countLetters(message);
		this.root = generateTree(this.letters);
		this.paths = generatePaths();
    }

	public BinaryWordTree() {
		 this(DEFAULT_SOURCE_MESSAGE);
	}

	public void initialize(String message) {
		this.letters =  countLetters(message);
		this.root = generateTree(this.letters);
		this.paths = generatePaths();
	}

	private static List<LetterNode> countLetters(String message) {
		/* Подсчёт используемых символов и их частоты появления */
		HashSet<Character> presentLetters = getLetters(message);
		char[] letters = new char[presentLetters.size()];
		int[] frequencies = new int[presentLetters.size()];
		int index = 0;
		for (char letter :
				presentLetters) {
			int frequency = countLetterFrequency(letter, message);
			letters[index] = letter;
			frequencies[index++] = frequency;
		}

		/* Создание ячеек символов */
		List<LetterNode> letterNodes = new ArrayList<>();
		for (int i = 0; i < letters.length; i++) {
			LetterNode node = new LetterNode(frequencies[i], letters[i]);
			letterNodes.add(node);
		}

		return letterNodes;
	}

	private static HashSet<Character> getLetters(String text) {
		HashSet<Character> foundLetters = new HashSet<>();
		for (int i = 0; i < text.length(); i++)
			foundLetters.add(text.charAt(i));
		return foundLetters;
	}

	private static int countLetterFrequency(char letter, String text) {
		int sum = -1;
		for (int i = 0; i < text.length(); i++) {
			char currentLetter = text.charAt(i);
			if (currentLetter == letter) {
				if (sum == -1)
					sum = 0;
				sum++;
			}
		}
		return sum;
	}

	private static LetterNode generateTree(List<LetterNode> letters) {
		 List<LetterNode> freeNodes = new ArrayList<>(letters);
		LetterNode up = null;
		while(!freeNodes.isEmpty()) {
			LetterNode left = findLowest(freeNodes);
			LetterNode right = findSecondLowest(freeNodes);
			if (right == null)
				break;

			up = new LetterNode(left.getValue() + right.getValue());
			up.setLeft(left);
			up.setRight(right);
			left.setUp(up);
			right.setUp(up);

			freeNodes.add(up);
			freeNodes.remove(left);
			freeNodes.remove(right);
		}

		 return up;
	}

	private static LetterNode findLowest(List<LetterNode> nodes) {
		if (nodes == null || nodes.isEmpty())
			return null;

		List<LetterNode> copy = new ArrayList<>(nodes);
		Collections.sort(copy);

		return copy.get(0);
	}

	private static LetterNode findSecondLowest(List<LetterNode> nodes) {
		if (nodes == null || nodes.isEmpty() || nodes.size() == 1)
			return null;

		List<LetterNode> copy = new ArrayList<>(nodes);
		Collections.sort(copy);

		return copy.get(1);
	}

	public HashMap<Character, List<Integer>> generatePaths() {
		 HashMap<Character, List<Integer>> paths = new HashMap<>();
		for (LetterNode letterNode :
				this.letters) {
			char letter = letterNode.getLetter();
			List<Integer> path = this.calculatePath(letter);
			paths.put(letter, path);
		}
		return paths;
	}

	public String encodeText(String text) {
		 StringBuilder encodedText = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char letter = text.charAt(i);
			encodedText.append(this.encodeletter(letter));
		}
		return encodedText.toString();
	}
	public String decodeText(String text) {
		 StringBuilder decodedText = new StringBuilder();
		 List<Integer> currentCode = new ArrayList<>();
		for (int i = 0; i < text.length(); i++) {
			currentCode.add(Integer.parseInt(String.valueOf(text.charAt(i))));
			Character foundCharacter = this.findByCode(currentCode);
			if (foundCharacter == null) {
				continue;
			}
			decodedText.append(foundCharacter);
			currentCode = new ArrayList<>();
		}
		 return decodedText.toString();
	}

	private Character findByCode(List<Integer> code) {
		for (Map.Entry<Character, List<Integer>> entry : this.paths.entrySet()) {
			if (code.equals(entry.getValue()))
				return entry.getKey();
		}
		 return null;
	}

	public char decodeletter(String letterRepresentation) {
		 LetterNode currentNode = this.root;
		for (int i = 0; i < letterRepresentation.length(); i++) {
			char turn = letterRepresentation.charAt(i);
			if (turn == '0')
				currentNode = currentNode.left;
			else
				currentNode = currentNode.right;
		}
		return currentNode.letter;
	}
	public String encodeletter(char letter) {
		 List<Integer> path = this.paths.get(letter);
		StringBuilder code = new StringBuilder();
		 for (Integer turn :
				path) {
			code.append(turn);
		}
		 return code.toString();
	}

	public List<Integer> getPath(char letter) {
		 return this.paths.get(letter);
	}
	private List<Integer> calculatePath(char letter) {
		 List<Integer> path = new ArrayList<>();
		 try {
			 path = recursiveCalculatePath(letter, path, this.root);
		 } catch (DeadEndException e) {
			path = null;
		 }
		 return path;
	}
	private List<Integer> recursiveCalculatePath(char letter, List<Integer> currentPath, LetterNode node) throws DeadEndException {
		if (node == null)
			throw new DeadEndException();
		 if (node.getLetter() != null && node.getLetter() == letter)
			return currentPath;
		// Проверяем слева
		currentPath.add(0);
		try {
			return recursiveCalculatePath(letter, currentPath, node.left);
		} catch (DeadEndException ignored) {}
		currentPath.remove(currentPath.size() - 1);

		// Проверяем справа
		currentPath.add(1);
		try {
			return recursiveCalculatePath(letter, currentPath, node.right);
		} catch (DeadEndException ignored) {}
		currentPath.remove(currentPath.size() - 1);

		throw new DeadEndException();
	}
}
