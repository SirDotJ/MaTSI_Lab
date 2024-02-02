package org.togu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UpdaterMain {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdaterMain.class);
	private static final long MINUTES_CHECK_INTERVAL; // How many minutes to wait in between each update check
	static {
		MINUTES_CHECK_INTERVAL = Long.parseLong(MetaInfo.getConfigurationInfo("update_interval_minutes"));
	}
	public static void main(String[] args) {
		startUpdateTimer();
	}
	private static void startUpdateTimer() {
		new Timer().schedule(
			new TimerTask() {
				@Override
				public void run() {
					update();
				}
			}, MINUTES_CHECK_INTERVAL * 60 * 1000
		);
	}
	private static void update() {
		try {
			if (CheckForUpdates.newerVersionAvailable()) {
				// TODO: ask user to update
				try {
					startUpdate(); // if answered yes
				} catch (IOException e) {
					LOGGER.error("Update failed", e);
					// TODO: show error
					startUpdateTimer();
				}
			}
		} catch (IOException e) {
			LOGGER.error("Wasn't able to check for updates, try again later or check your internet connection");
			startUpdateTimer();
		}
	}
	private static final String UPDATER_INITIALIZER_SCRIPT_FILE_NAME = "start_initialize_update.bat";
	private static void startUpdate() throws IOException {
		Process updateInitializer;
		try {
			ProcessBuilder updateInitializerBuilder = new ProcessBuilder();
			updateInitializerBuilder.command(UPDATER_INITIALIZER_SCRIPT_FILE_NAME);
			updateInitializerBuilder.inheritIO();
			updateInitializer = updateInitializerBuilder.start();
		} catch (IOException e) {
			LOGGER.error("Couldn't start the update due to IOException", e);
			return;
		}
		System.exit(0);
	}
}