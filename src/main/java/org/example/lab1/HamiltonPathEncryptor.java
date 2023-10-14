package org.example.lab1;

import org.example.common.Decryptor;
import org.example.common.Encryptor;

import java.util.ArrayList;
import java.util.List;

// Класс определяет шифровку методом "Перестановки по гамильтоновым путям"
public class HamiltonPathEncryptor implements Encryptor, Decryptor {
	private List<Integer> hamiltonPath;
	final private List<ArrayList<Integer>> adjacencyMatrix;
	final private List<Integer> vertices;

	public HamiltonPathEncryptor(List<ArrayList<Integer>> adjacencyMatrix, List<Integer> hamiltonPath) throws IllegalArgumentException {
		this.adjacencyMatrix = adjacencyMatrix;
		this.vertices = new ArrayList<>();
		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			this.vertices.add(i);
		}
		this.hamiltonPath = hamiltonPath;
	}
	public HamiltonPathEncryptor(List<ArrayList<Integer>> adjacencyMatrix) throws IllegalArgumentException {
		this.adjacencyMatrix = adjacencyMatrix;
		this.vertices = new ArrayList<>();
		for (int i = 0; i < adjacencyMatrix.size(); i++) {
			this.vertices.add(i);
		}
		this.hamiltonPath = this.generateHamiltonPath();
	}

	public List<Integer> getHamiltonPath() {
		return hamiltonPath;
	}
	public void setHamiltonPath(List<Integer> hamiltonPath) {
		this.hamiltonPath = hamiltonPath;
	}

	private List<Integer> getAvailableVertices(List<Integer> excludedVertices, int vertex) {
		List<Integer> availableVertices = new ArrayList<>();
		List<Integer> adjacentVertices = this.adjacencyMatrix.get(vertex);
		for (int i = 0; i < adjacentVertices.size(); i++) {
			if(adjacentVertices.get(i) == 1 && !excludedVertices.contains(i))
				availableVertices.add(i);
		}
		return availableVertices;
	}
	// {-1} значит передали пустой массив (доступных вершин нет)
	private static int getRandomVertice(List<Integer> availableVertices) {
		if (availableVertices == null || availableVertices.isEmpty())
			return -1;
		int lowerBound = 0;
		int upperBound = availableVertices.size() - 1;
		return availableVertices.get((int) ((Math.random() * (upperBound - lowerBound)) + lowerBound));
	}
	// Предполагается, что граф должен иметь хотя бы один гамильтоновый путь
	private List<Integer> recursiveFindHamiltonPath(List<Integer> currentHamiltonPath, List<Integer> availableVertices) {
		// Проверка нахождения гамильтонового пути
		if(currentHamiltonPath.size() == this.vertices.size())
			return currentHamiltonPath;

		// Проверка на существование вершин к рассмотрению
		if (availableVertices.isEmpty())
			return currentHamiltonPath;

		// Добавляем к пути следующую случайную доступную вершину
		int chosenVertex = getRandomVertice(availableVertices);
		currentHamiltonPath.add(chosenVertex);
		List<Integer> newHamiltonPathVariant = new ArrayList<>(currentHamiltonPath);
		List<Integer> newAvailableVertices = this.getAvailableVertices(newHamiltonPathVariant, chosenVertex);
		return recursiveFindHamiltonPath(newHamiltonPathVariant, newAvailableVertices);
	}
	private boolean pathIsValid(List<Integer> potentialHamiltonPath) {
		// Путь должен проходить через все вершины
		if(potentialHamiltonPath.size() != this.vertices.size())
			return false;
		// Путь не может быть 0, 1, 2, ..., N
		for (int i = 0; i < potentialHamiltonPath.size(); i++)
			if (potentialHamiltonPath.get(i) != i)
				return true; // путь не базовый
		return false;
	}
	public List<Integer> generateHamiltonPath() {
		List<Integer> newHamiltonPath = new ArrayList<>();
		do {
			for (int i = 0; i < this.vertices.size(); i++) {
			newHamiltonPath.add(i);
			List<Integer> availableVertices = this.getAvailableVertices(newHamiltonPath, i);
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

		if (message.length() % this.hamiltonPath.size() != 0) {
			int spacesToAdd = this.hamiltonPath.size() - message.length() % this.hamiltonPath.size();
			message = message + "_".repeat(Math.max(0, spacesToAdd));
		}

		int substringLength = this.hamiltonPath.size();
		int substringCount = message.length() / substringLength;

		for (int i = 0; i < substringCount; i++) {
			StringBuilder substring = new StringBuilder();
			int offset = i * substringLength;
			for (Integer substringIndex : this.hamiltonPath)
				substring.append(message.charAt(offset + substringIndex));
			encryptedMessage.append(substring);
		}

		return encryptedMessage.toString();
	}
	@Override
	public String decrypt(String message) throws IllegalArgumentException {
		StringBuilder decryptedMessage = new StringBuilder();

		if (message.length() % this.hamiltonPath.size() != 0)
			throw new IllegalArgumentException("Decrypted message must be consistent with the key path");

		int substringLength = this.hamiltonPath.size();
		int substringCount = message.length() / substringLength;

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

		return decryptedMessage.toString();
	}
}
