package org.example.lab1;

import org.example.common.Encryptor;

import java.util.ArrayList;

// Класс определяет шифровку методом "Перестановки по гамильтоновым путям"
public class HamiltonPathEncryptor implements Encryptor {
	private ArrayList<Integer> hamiltonPath = new ArrayList<>();
	final private ArrayList<ArrayList<Integer>> adjacencyMatrix;
	final private ArrayList<Integer> vertices;
	public HamiltonPathEncryptor(ArrayList<ArrayList<Integer>> adjacencyMatrix, ArrayList<Integer> hamiltonPath) throws IllegalArgumentException {
		this.adjacencyMatrix = adjacencyMatrix;
		this.vertices = new ArrayList<>();
		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			this.vertices.add(i);
		}
		this.hamiltonPath = hamiltonPath;
	}
	public HamiltonPathEncryptor(ArrayList<ArrayList<Integer>> adjacencyMatrix) throws IllegalArgumentException {
		this.adjacencyMatrix = adjacencyMatrix;
		this.vertices = new ArrayList<>();
		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			this.vertices.add(i);
		}
		this.hamiltonPath = this.generateHamiltonPath();
	}
	// Нахождение гамильтонового пути (предполагается, что граф должен иметь хотя бы один такой путь)
	public ArrayList<Integer> generateHamiltonPath() {
		ArrayList<Integer> newHamiltonPath = new ArrayList<>();
		do {
			for (int i = 0; i < this.vertices.size(); i++) {
			newHamiltonPath.add(i);
			ArrayList<Integer> availableVertices = this.getAvailableVertices(newHamiltonPath, i);
			newHamiltonPath = recursiveFindHamiltonPath(newHamiltonPath, availableVertices);
			if(newHamiltonPath.size() == this.vertices.size())
				break;
			newHamiltonPath.clear();
			}
		} while (!pathIsValid(newHamiltonPath));
		return newHamiltonPath;
	}

	@Override
	public String encrypt(String message) {
		StringBuilder encryptedMessage = new StringBuilder();

		int substringLength = this.hamiltonPath.size();
		int substringCount = message.length() / substringLength;
		int leftoverCount = message.length() % substringLength;

		for (int i = 0; i < substringCount; i++) {
			StringBuilder substring = new StringBuilder();
			int offset = i * substringLength;
			for (Integer substringIndex : this.hamiltonPath)
				substring.append(message.charAt(offset + substringIndex));
			encryptedMessage.append(substring);
		}
		// Остаток сообщения, не полностью вмещающийся в размерность ключа
		int offset = substringLength * substringCount;
		StringBuilder leftover = new StringBuilder();
		for (int i = 0; i < leftoverCount; i++) {
			leftover.append(message.charAt(offset + this.hamiltonPath.get(i)));
		}
		encryptedMessage.append(leftover);

		return encryptedMessage.toString();
	}
	@Override
	public String decrypt(String message) {
		StringBuilder decryptedMessage = new StringBuilder();

		int substringLength = this.hamiltonPath.size();
		int substringCount = message.length() / substringLength;
		int leftoverCount = message.length() % substringLength;

		// Шаблон, используется для определения подстрок и их непоследовательного заполнения
		StringBuilder substringTemplate = new StringBuilder();
		substringTemplate.append("-".repeat(substringLength));

		for (int i = 0; i < substringCount; i++) {
			StringBuilder substring = new StringBuilder(substringTemplate);
			int offset = i * substringLength;
			for (int j = 0; j < substringLength; j++) {
				substring.setCharAt(this.hamiltonPath.get(j), message.charAt(offset + j));
			}
			decryptedMessage.append(substring);
		}

		// Остаток сообщения, не полностью вмещающийся в размерность ключа
		int offset = substringLength * substringCount;
		StringBuilder leftover = new StringBuilder();
		leftover.append("-".repeat(leftoverCount));
		for (int i = 0; i < leftoverCount; i++) {
			leftover.setCharAt(this.hamiltonPath.get(i), message.charAt(offset + i));
		}
		decryptedMessage.append(leftover);

		return decryptedMessage.toString();
	}
	public void printHamiltonPath() {
		System.out.println(this.hamiltonPath.toString());
	}
	private ArrayList<Integer> recursiveFindHamiltonPath(ArrayList<Integer> currentHamiltonPath, ArrayList<Integer> availableVertices) {
		// Проверка нахождения гамильтонового пути
		if(currentHamiltonPath.size() == this.vertices.size())
			return currentHamiltonPath;

		// Проверка на существование вершин к рассмотрению
		if (availableVertices.isEmpty())
			return currentHamiltonPath;

		// Добавляем к пути следующую случайную доступную вершину
		int chosenVertex = getRandomVertice(availableVertices);
		currentHamiltonPath.add(chosenVertex);
		ArrayList<Integer> newHamiltonPathVariant = new ArrayList<>(currentHamiltonPath);
		ArrayList<Integer> newAvailableVertices = this.getAvailableVertices(newHamiltonPathVariant, chosenVertex);
		return recursiveFindHamiltonPath(newHamiltonPathVariant, newAvailableVertices);
	}
	private boolean pathIsValid(ArrayList<Integer> potentialHamiltonPath) {
		// Путь должен проходить через все вершины
		if(potentialHamiltonPath.size() != this.vertices.size())
			return false;
		// Путь не может быть 0, 1, 2, ..., N
		for (int i = 0; i < potentialHamiltonPath.size(); i++)
			if (potentialHamiltonPath.get(i) != i)
				return true; // путь не базовый
		return false;
	}
	private ArrayList<Integer> getAvailableVertices(ArrayList<Integer> excludedVertices, int vertex) {
		ArrayList<Integer> availableVertices = new ArrayList<>();
		ArrayList<Integer> adjacentVertices = this.adjacencyMatrix.get(vertex);
		for (int i = 0; i < adjacentVertices.size(); i++) {
			if(adjacentVertices.get(i) == 1 && !excludedVertices.contains(i))
				availableVertices.add(i);
		}
		return availableVertices;
	}
	// -1 значит передали пустой массив (доступных вершин нет)
	private static int getRandomVertice(ArrayList<Integer> availableVertices) {
		if (availableVertices == null || availableVertices.isEmpty())
			return -1;
		int lowerBound = 0;
		int upperBound = availableVertices.size() - 1;
		return availableVertices.get((int) ((Math.random() * (upperBound - lowerBound)) + lowerBound));
	}

	public ArrayList<Integer> getHamiltonPath() {
		return hamiltonPath;
	}

	public void setHamiltonPath(ArrayList<Integer> hamiltonPath) {
		this.hamiltonPath = hamiltonPath;
	}
}
