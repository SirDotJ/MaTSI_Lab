package org.togu.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.togu.FileParser;
import org.togu.MetaInfo;
import org.togu.lab7.Rabin;

import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class FeedbackBot extends TelegramLongPollingBot {
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackBot.class);
	private static final Decryptor DECRYPTOR = new Rabin(AlphabetConstants.FULL_KEYBOARD, 16943, 6883, 57185);

	private static long getDeveloperId() {
		try {
			return Long.parseLong(DECRYPTOR.decrypt(MetaInfo.getAPIInfo("feedback_bot", "encrypted_developer_id")));
		} catch (NumberFormatException e) {
			LOGGER.error("No valid long id was found for stored metadata \"encrypted_developer_id\"");
			throw new IllegalStateException("Set encrypted_developer_id in apiInfo.json was not able to be parsed to long");
		}
	}
	@Override
	public String getBotUsername() {
		return MetaInfo.getAPIInfo("feedback_bot", "username");
	}

	@Override
	public String getBotToken() {
		return DECRYPTOR.decrypt(MetaInfo.getAPIInfo("feedback_bot", "encrypted_id"));
	}

	@Override
	public void onUpdateReceived(Update update) {
		// Bot shouldn't react to messages
	}
	public void sendMessage(String message) throws IllegalStateException, TelegramApiException {
		if (!internetAvailable()) {
			LOGGER.error("No internet connection while trying to send message");
			throw new IllegalStateException("No internet connection available");
		}

		SendMessage sentMessage = new SendMessage();
		sentMessage.setChatId(getDeveloperId());
		sentMessage.setText(message);

		try {
			this.execute(sentMessage);
		} catch (TelegramApiException e) {
			LOGGER.error("TelegramApiException occurred while trying to send a message", e);
			throw e;
		}
	}
	public void sendFile(String filePath) throws IllegalStateException, NullPointerException, TelegramApiException {
		if (!internetAvailable()) {
			LOGGER.error("No internet connection while trying to send file");
			throw new IllegalStateException("No internet connection available");
		}

		File file;
		try {
			file = FileParser.readLocalFile(filePath);
		} catch (NullPointerException e) {
			LOGGER.error(String.format(String.format("Null received while trying to read file under path \"%s\"", filePath)), e);
			throw e;
		}

		SendDocument messageWithFile = new SendDocument();
		messageWithFile.setChatId(getDeveloperId());
		messageWithFile.setDocument(new InputFile(file));

		try {
			this.execute(messageWithFile);
		} catch (TelegramApiException e) {
			LOGGER.error("TelegramApiException occurred while trying to send a message", e);
			throw e;
		}
	}

	private static boolean internetAvailable() {
		Enumeration<NetworkInterface> availableNetworkInterfaces;
        try {
            availableNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {  // I/O exception or no single configured network set
            return false;
        }
        while (availableNetworkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = availableNetworkInterfaces.nextElement();
            try {
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    return true;
                }
            } catch (SocketException ignore) {  // I/O exception occurred
            }
        }
        return false;  // Networks are configured but none of them are running and not in loopback
	}
}
