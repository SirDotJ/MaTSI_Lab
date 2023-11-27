package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainApplicationController {
	@FXML
	private AnchorPane methodDisplay; // Область отображения загруженной формы метода шифровки пользователю

	/* Методы шифровки */

	@FXML
	private RadioButton caesarMethodButton; // Кнопка выбора загрузки шифра "Цезаря" пользователем
	@FXML
	private RadioButton rearrangementMethodButton; // Кнопка выбора загрузки шифра "перестановкой" пользователем
	@FXML
	private RadioButton hamiltonMethodButton; // Кнопка выбора загрузки шифра "гамильтонового пути" пользователем
	@FXML
	private RadioButton vigenereMethodButton; // Кнопка выбора загрузки шифра "Виженера" пользователем
	@FXML
	private RadioButton bofortMethodButton; // Кнопка выбора загрузки шифра "Бофорта" пользователем
	@FXML
	private RadioButton matrixMultiplicationMethodButton; // Кнопка выбора загрузки шифра "умножением матриц" пользователем
	@FXML
	private RadioButton knapsackMethodButton; // Кнопка выбора загрузки шифра "укладки ранца" пользователем
	@FXML
	private RadioButton playfairMethodButton; // Кнопка выбора загрузки шифра "Плейфера" пользователем
	@FXML
	private RadioButton RSAMethodButton; // Кнопка выбора загрузки криптосистемы "RSA" пользователем
	@FXML
	private RadioButton ElGamalMethodButton; // Кнопка выбора загрузки криптосистемы "Эль-Гамаля" пользователем

	/* Методы сжатия */

	@FXML
	private RadioButton RLEMethodButton; // Кнопка выбора загрузки сжатия "RLE" пользователем
	@FXML
	private RadioButton BWTMethodButton; // Кнопка выбора загрузки сжатия "BWT" пользователем
	@FXML
	private RadioButton BWTAndRLEMethodButton; // Кнопка выбора загрузки сжатия "BWT + RLE" пользователем
	@FXML
	private RadioButton HuffmanMethodButton; // Кнопка выбора загрузки сжатия "Хаффмана" пользователем

	/* Методы сохранения целостности данных */
	@FXML
	private RadioButton HemmingMethodButton; // Кнопка выбора загрузки метода сохранения целостности "Код Хэмминга" пользователем

	private void setDisplayScene(String fxmlFileName) {
		this.methodDisplay.getChildren().clear();
		Node leafNode;
		try {
			leafNode = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlFileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.methodDisplay.getChildren().addAll(leafNode);
	}
	/* Методы шифровки */

	@FXML
	public void loadCaesarMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Алгоритм Цезаря" пользователем
		if (caesarMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_CaesarMain.fxml");
		}
	}
	@FXML
	public void loadRearrangementMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Перестановка" пользователем
		if (rearrangementMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_RearrangementMain.fxml");
		}
	}
	@FXML
	public void loadHamiltonMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Гамильтоновый путь" пользователем
		if (hamiltonMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_HamiltonPathMain.fxml");
		}
	}
	@FXML
	public void loadVigenereMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Шифр Виженера" пользователем
		if (vigenereMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_VigenereMain.fxml");
		}
	}
	@FXML
	public void loadBofortMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Шифр Бофорта" пользователем
		if(bofortMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_BofortMain.fxml");
		}
	}
	@FXML
	public void loadMatrixMultiplicationMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Шифр умножением матриц" пользователем
		if(matrixMultiplicationMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_MatrixMultiplicationMain.fxml");
		}
	}
	@FXML
	public void loadKnapsackMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Шифр укладки ранца" пользователем
		if(knapsackMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_KnapsackMain.fxml");
		}
	}
	@FXML
	public void loadPlayfairMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Шифр Плейфера" пользователем
		if(playfairMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_PlayfairMain.fxml");
		}
	}
	@FXML
	public void loadRSAMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Криптосистема RSA" пользователем
		if (RSAMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_RSAMain.fxml");
		}
	}
	@FXML
	public void loadElGamalMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Криптосистема Эль-Гамаля" пользователем
		if (ElGamalMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_ElGamalMain.fxml");
		}
	}

	/* Методы сжатия */

	@FXML
	public void loadRLEMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Метод RLE" пользователем
		if (RLEMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_RLEMain.fxml");
		}
	}
	@FXML
	public void loadBWTMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Метод BWT" пользователем
		if (BWTMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_BWTMain.fxml");
		}
	}
	@FXML
	public void loadBWTAndRLEMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Метод BWT + RLE" пользователем
		if (BWTAndRLEMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_BWTAndRLEMain.fxml");
		}
	}
	@FXML
	public void loadHuffmanMethod() { // Вызывается при выборе радио кнопки "Алгоритм Хаффмана" пользователем
		if (HuffmanMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_HuffmanMain.fxml");
		}
	}

	/* Методы сохранения целостности */
	@FXML
	public void loadHemmingMethod(ActionEvent event) { // Вызывается при выборе радио кнопки "Код Хэмминга" пользователем
		if (HemmingMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_HemmingMain.fxml");
		}
	}
}
