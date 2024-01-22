package org.togu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	private static final String MAIN_APPLICATION_SCRIPT_FILE_NAME = "start_main.bat";
	private static final String UPDATER_SCRIPT_FILE_NAME = "start_updater.bat";
	public static void main(String[] args) {
		// Used to specify logback.xml file to be used: one in the resource path, and not in modules this module depends on
		System.setProperty("logback.configurationFile", "logback.xml");
		LOGGER.info("Starting Launcher...");

		Process mainApplication;
		try {
			ProcessBuilder mainApplicationBuilder = new ProcessBuilder();
			mainApplicationBuilder.command(MAIN_APPLICATION_SCRIPT_FILE_NAME);
			mainApplicationBuilder.inheritIO();
			mainApplication = mainApplicationBuilder.start();
		} catch (IOException e) {
			LOGGER.error(String.format("Failed to launch main application using script file: \"%s\"", MAIN_APPLICATION_SCRIPT_FILE_NAME), e);
			// TODO: show main application start error message to user
			return;
		}
		Process updater;
		try {
			ProcessBuilder updaterBuilder = new ProcessBuilder();
			updaterBuilder.command(UPDATER_SCRIPT_FILE_NAME);
			updaterBuilder.inheritIO();
			updater = updaterBuilder.start();
		} catch (IOException e) {
			LOGGER.error(String.format("Failed to launch updater using script file: \"%s\"", UPDATER_SCRIPT_FILE_NAME), e);
			return;
		}
		LOGGER.info("Launcher started applications successfully!");
	}
}