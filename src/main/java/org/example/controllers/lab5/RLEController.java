package org.example.controllers.lab5;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.example.common.DecoderForm;
import org.example.common.EncoderForm;
import org.example.common.HelpController;
import org.example.lab5.RLE;

public class RLEController implements EncoderForm, DecoderForm {
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputMessage; // Текстовая область вывода результата шифровки пользователю
	@FXML
	Text differenceDisplay; // Текст вывода результата проведения сжатия

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

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputMessage.getText());
		this.outputMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Метод RLE", "RLEDescription.md");
	}
}
