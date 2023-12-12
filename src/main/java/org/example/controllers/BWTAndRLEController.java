package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.example.common.DecoderForm;
import org.example.common.EncoderForm;
import org.example.lab5.BWT;
import org.example.lab5.RLE;

public class BWTAndRLEController implements EncoderForm, DecoderForm {
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	RadioButton radioButtonYesBWT; // Радио кнопка "Вместе с BWT"
	@FXML
	RadioButton radioButtonNoBWT; // Радио кнопка "Без BWT"

	@FXML
	TextArea outputMessage; // Текстовая область вывода результата шифровки пользователю
	@FXML
	Text differenceDisplay; // Текст вывода результата проведения сжатия

	BWT bwt = new BWT();
	RLE rle = new RLE();
	private boolean usesBWT = false;
	public void activateBWT() {
		if (radioButtonYesBWT.isSelected())
			this.usesBWT = true;
	}
	public void deactivateBWT() {
		if (radioButtonNoBWT.isSelected())
			this.usesBWT = false;
	}
	@Override
	public void encode() {
		String plainMessage = inputMessage.getText();
		if (this.usesBWT)
			plainMessage = bwt.encode(plainMessage);
		String compressedMessage = rle.encode(plainMessage);

		this.outputMessage.setText(compressedMessage);
		this.differenceDisplay.setText("Разница: " + (plainMessage.length() - compressedMessage.length()));
	}

	@Override
	public void decode() {
		String compressedMessage = inputMessage.getText();
		String plainMessage = rle.decode(compressedMessage);
		if (this.usesBWT)
			plainMessage = bwt.decode(plainMessage);

		this.outputMessage.setText(plainMessage);
		this.differenceDisplay.setText("Разница: " + (plainMessage.length() - compressedMessage.length()));
	}

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputMessage.getText());
		this.outputMessage.setText("");
	}

	public void openHelp() {
		return;
	}
}
