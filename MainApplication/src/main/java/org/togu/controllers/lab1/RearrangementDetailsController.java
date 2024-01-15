package org.togu.controllers.lab1;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class RearrangementDetailsController {
	@FXML
	TextArea encryptionTableOutput; // Текстовая область вывода таблицы зашифровки пользователю
	@FXML
	TextArea decryptionTableOutput; // Текстовая область вывода таблицы расшифровки пользователю
	public void displayEncryptionTable(String table) {
		encryptionTableOutput.setText(table);
	}
	public void displayDecryptionTable(String table) {
		decryptionTableOutput.setText(table);
	}
}
