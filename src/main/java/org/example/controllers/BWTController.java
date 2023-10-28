package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.example.common.DecoderForm;
import org.example.common.EncoderForm;
import org.example.lab5.BWT;

public class BWTController implements EncoderForm, DecoderForm {
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputMessage; // Текстовая область вывода результата шифровки пользователю

	BWT encoder = new BWT();
	@Override
	public void encode() {
		String plainMessage = inputMessage.getText();
		String compressedMessage = encoder.encode(plainMessage);
		this.outputMessage.setText(compressedMessage);
	}

	@Override
	public void decode() {
		String compressedMessage = inputMessage.getText();
		String plainMessage = encoder.decode(compressedMessage);
		this.outputMessage.setText(plainMessage);
	}
}
