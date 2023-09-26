package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class RearrangementDetailsController {
	@FXML
	TextArea encryptionTableOutput;
	@FXML
	TextArea decryptionTableOutput;
	public void displayEncryptionTable(String table) {
		encryptionTableOutput.setText(table);
	}
	public void displayDecryptionTable(String table) {
		decryptionTableOutput.setText(table);
	}
}
