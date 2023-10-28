package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.common.Alerts;
import org.example.lab5.HemmingCode;

public class HemmingController {
	@FXML
	TextArea inputMessage; // Текстовая область заполнения сообщения для шифровки пользователем
	@FXML
	TextArea outputMessage; // Текстовая область вывода результата шифровки пользователю
	@FXML
	TextField subdivisionCountMessage; // Текстовое поле ввода количества разделения одного сообщения

	HemmingCode code = new HemmingCode();

	public void secure() {
		String plainMessage = inputMessage.getText();

		if (plainMessage.isEmpty()) {
			Alerts.showError(
					"Ошибка создания сообщения",
					"Нет введенного сообщения",
					"Пожалуйста введите сообщение в поле \"Текущее сообщение\" и повторите попытку"
			);
			return;
		}
		int subdivisionCount;
		try {
			subdivisionCount = Integer.parseInt(this.subdivisionCountMessage.getText());
		} catch (Exception e) {
			Alerts.showError(
					"Ошибка чтения количества разделений",
					"Произошла ошибка во время чтения количества символов в разделении",
					"Сообщение об ошибке: " + e + ", пожалуйста введите число в поле \"Кол-во символов в проверке\"."
			);
			return;
		}

		this.code.secure(plainMessage, subdivisionCount);
		this.inputMessage.setText(this.code.getMessage());
		this.outputMessage.appendText("Сообщение: " + plainMessage + " преобразовано в двоичный код: " + this.code.getMessage() + "\n");
	}

	public void corrupt() {
		if (this.code == null) {
			Alerts.showError(
					"Ошибка загрязнения данных",
					"Нет установленного сообщения",
					"Пожалуйста введите и преобразуйте сообщение и повторите попытку"
			);
			return;
		}
		int[] corruptedIndexes = this.code.corruptMessage();
		this.inputMessage.setText(this.code.getMessage());
		this.outputMessage.appendText("Сообщение было испорчено на позициях: ");
		for (int corruptedIndex : corruptedIndexes) {
			this.outputMessage.appendText(corruptedIndex + ", ");
		}
		this.outputMessage.appendText("\nСообщение теперь имеет вид: " + this.code.getMessage() + "\n");
	}

	public void fix() {
		if (this.code == null) {
			Alerts.showError(
					"Ошибка исправления данных",
					"Нет установленного сообщения",
					"Пожалуйста введите и преобразуйте сообщение и повторите попытку"
			);
			return;
		}
		this.code.fix();
		this.inputMessage.setText(this.code.getMessage());
		this.outputMessage.appendText("Сообщение было исправлено и теперь имеет вид:\n" + this.code.getMessage());
	}
}
