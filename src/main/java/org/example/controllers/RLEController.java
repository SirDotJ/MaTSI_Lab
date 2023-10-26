package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.example.common.DecoderForm;
import org.example.common.EncoderForm;
import org.example.lab5.RLE;

public class RLEController implements EncoderForm, DecoderForm {
	@FXML
	TextArea inputMessage;
	@FXML
	TextArea outputMessage;
	@FXML
	Text differenceDisplay;

	RLE encoder = new RLE();

	@Override
	public void encode() {
		String plainMessage = inputMessage.getText();
		String compressedMessage = encoder.encode(plainMessage);
		this.outputMessage.setText(compressedMessage);
		this.differenceDisplay.setText("Разница: " + (plainMessage.length() - compressedMessage.length()));
	}

	@Override
	public void decode() {
		String compressedMessage = inputMessage.getText();
		String plainMessage = encoder.decode(compressedMessage);
		this.outputMessage.setText(plainMessage);
		this.differenceDisplay.setText("Разница: " + (plainMessage.length() - compressedMessage.length()));
	}
}
