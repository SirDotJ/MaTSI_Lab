package org.togu.controllers.lab5;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.togu.common.DecoderForm;
import org.togu.common.EncoderForm;
import org.togu.controllers.HelpController;
import org.togu.lab5.HuffmanAlgorithm;

public class HuffmanController implements EncoderForm, DecoderForm {
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputMessage; // Текстовая область вывода результата шифровки пользователю

	HuffmanAlgorithm encoder = new HuffmanAlgorithm();

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

	public void transferOutputToInput() {
		this.inputMessage.setText(this.outputMessage.getText());
		this.outputMessage.setText("");
	}

	public void openHelp() {
		HelpController.open("Алгоритм Хаффмана", "HuffmanDescription.md");
	}
}
