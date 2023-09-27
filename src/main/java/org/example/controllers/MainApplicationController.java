package org.example.controllers;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainApplicationController {
	@FXML
	private AnchorPane methodDisplay;
	@FXML
	private RadioButton caesarMethodButton;
	@FXML
	private RadioButton rearrangementMethodButton;
	@FXML
	private RadioButton hamiltonMethodButton;
	@FXML
	private RadioButton vigenereMethodButton;
	@FXML
	private RadioButton bofortMethodButton;
	@FXML
	private RadioButton matrixMultiplicationMethodButton;
	@FXML
	private RadioButton knapsackMethodButton;
	@FXML
	private void setDisplayScene(String fxmlFileName) {
		this.methodDisplay.getChildren().clear();
		Node leafNode = null;
		try {
			leafNode = FXMLLoader.load(getClass().getClassLoader().getResource(fxmlFileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.methodDisplay.getChildren().addAll(leafNode);
	}
	@FXML
	public void loadCaesarMethod(ActionEvent event) throws IOException {
		if (caesarMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_CaesarMain.fxml");
		}
	}
	@FXML
	public void loadRearrangementMethod(ActionEvent event) {
		if (rearrangementMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_RearrangementMain.fxml");
		}
	}
	@FXML
	public void loadHamiltonMethod(ActionEvent event) {
		if (hamiltonMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_HamiltonPathMain.fxml");
		}
	}
	@FXML
	public void loadVigenereMethod(ActionEvent event) {
		if (vigenereMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_VigenereMain.fxml");
		}
	}
	@FXML
	public void loadBofortMethod(ActionEvent event) {
		if(bofortMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_BofortMain.fxml");
		}
	}
	@FXML
	public void loadMatrixMultiplicationMethod(ActionEvent event) {
		if(matrixMultiplicationMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_MatrixMultiplicationMain.fxml");
		}
	}
	@FXML
	public void loadKnapsackMethod(ActionEvent event) {
		if(knapsackMethodButton.isSelected()) {
			this.setDisplayScene("MaTDP_KnapsackMain.fxml");
		}
	}
}
