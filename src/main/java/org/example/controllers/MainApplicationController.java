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
}
