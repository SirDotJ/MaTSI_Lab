package org.togu.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.togu.common.Alerts;
import org.togu.common.FeedbackBot;

import java.io.IOException;
import java.util.List;

public class CommunicationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommunicationController.class);

	private static final String FILL_OUT_FORM_FILE_NAME = "/CommunicationForm.fxml";

	private static final List<String> TRACEBACK_FILE_NAMES = List.of(
		"traceback-Launcher.log",
		"traceback-MainApplication.log",
		"traceback-Updater.log",
		"traceback-UpdateInitializer.log",
		"traceback-UpdaterInitializer.log"
	);

	public static void open() {
		Stage stage = new Stage();

		FXMLLoader loader = new FXMLLoader(CommunicationController.class.getResource(FILL_OUT_FORM_FILE_NAME));

		Parent root;
		try {
			root = loader.load();
		} catch (IOException e) {
			LOGGER.error(String.format("While trying to load form under resource path: \"%s\"", FILL_OUT_FORM_FILE_NAME), e);
			Alerts.showError(
				"Loading error",
				"Exception occurred while trying to load communication form",
				e.toString()
			);
			return;
		}

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	TextField messageHeader;

	@FXML
	TextArea messageContent;

	@FXML
	TextField contact;

	public void send() {
		if (messageHeader.getText().isEmpty()) {
			LOGGER.warn("Tried to send a message without filling out the header");
			Alerts.showError(
				"Ошибка оформления сообщения",
				"Пустое поле темы сообщения",
				"Пожалуйста заполните тему сообщения"
			);
			return;
		} else if (messageContent.getText().isEmpty()) {
			LOGGER.warn("Tried to send a message without filling out content");
			Alerts.showError(
				"Ошибка оформления сообщения",
				"Пустое сообщение",
				"Пожалуйста заполните содержимое сообщения"
			);
			return;
		}

		String messageToSend = buildMessage(messageHeader.getText(), messageContent.getText(), contact.getText());

		FeedbackBot messenger;
		try {
			messenger = new FeedbackBot();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while trying to make a FeedbackBot instance", e);
			Alerts.showError(
				"Ошибка библиотеки telegrambots",
				"Во время создания отправителя возникла ошибка",
				"Сообщите о данной ошибке по адресу Telegram: https://t.me/SirDotJ либо отправьте сообщение на Электронную почту: 2020102045@pnu.edu.ru"
			);
			return;
		}

		try {
			messenger.sendMessage(messageToSend);
		} catch (IllegalStateException e) {  // No internet
			LOGGER.warn("No internet while trying to send message");
			Alerts.showError(
				"Ошибка отправки",
				"Нет подключения к интернету",
				"Пожалуйста проверьте связь с интернетом и попробуйте ещё раз"
			);
			return;
		} catch (TelegramApiException e) {  // TelegramAPI error
			LOGGER.error("TelegramAPIException while trying to send message", e);
			Alerts.showError(
				"Ошибка отправки",
				"Проблема связи с Telegram",
				"Попробуйте отправить сообщение ещё раз, если проблема остаётся сообщите об этом по адресу Telegram: https://t.me/SirDotJ либо отправьте сообщение на Электронную почту: 2020102045@pnu.edu.ru"
			);
			return;
		}

		for (String tracebackFileName : TRACEBACK_FILE_NAMES) {
			try {
				messenger.sendFile(tracebackFileName);
			} catch (IllegalStateException e) {  // No internet
				LOGGER.warn(String.format("No internet while trying to send file: \"%s\"", tracebackFileName));
				Alerts.showError(
					"Нет связи",
					"Нет подключения к интернету",
					"Пожалуйста проверьте связь с интернетом и повторите попытку."
				);
				return;
			} catch (TelegramApiException e) {  // TelegramAPI error
				LOGGER.error(String.format("TelegramAPIException while trying to send file: \"%s\"", tracebackFileName));
				try {
					messenger.sendMessage(String.format("File could not be sent: \"%s\"", tracebackFileName));
				} catch (Exception ignore) {}
			} catch (NullPointerException e) {  // Files not found
				LOGGER.error(String.format("File not found under provided path: \"%s\"", tracebackFileName));
				try {
					messenger.sendMessage(String.format("Файл \"%s\" не был найден!", tracebackFileName));
				} catch (Exception ignore) {}
			}
		}
		Alerts.showError( // TODO: replace with information instead of error
			"Успешная отправка",
			"Сообщение было отправлено",
			"Сообщение было успешно отправлено разработчику приложения"
		);
	}

	private static String buildMessage(String header, String content, String contact) {
		StringBuilder builder = new StringBuilder();

		builder.append("Тема: ").append(header).append("\n");
		builder.append("Содержимое: ").append(content).append("\n");
		if (contact.isEmpty())
			builder.append("Контактные данные не были приложены.");
		else
			builder.append("Контактные данные: ").append(contact);

		return builder.toString();
	}

}
