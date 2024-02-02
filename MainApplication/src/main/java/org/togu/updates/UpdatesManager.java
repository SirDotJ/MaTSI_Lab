package org.togu.updates;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class UpdatesManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdatesManager.class);

	public static UpdatesManager singleton = null;
	private final UpdateChecker checker;

	private final long updateInterval; // In Milliseconds
	UpdatesManager(long infoInterval, long updateInterval) {
		this.checker = new UpdateChecker(infoInterval);
		this.updateInterval = updateInterval;
	}

	private static final String UPDATE_INITIALIZER_LAUNCH_SCRIPT_FILE_NAME = "start_update_initializer.bat";
	/**
	 * Checks up on checker and if there is an update available starts the updating process.
	 */
	public void update() {
		try {
			if (!this.checker.newerReleaseAvailable()) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Platform.runLater(() -> {
							update();
						});
					}
				}, this.updateInterval);
				return;
			}
		} catch (IOException e) {
			LOGGER.error("Could not determine newer release availability");
			return;
		}

		LOGGER.info("Newer version is available");
		String updatePackageDownloadUrl;
		try {
			updatePackageDownloadUrl = this.checker.getLatestDownloadLink();
		} catch (FileNotFoundException e) {
			LOGGER.error("Latest release does not contain necessary files to perform an update");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> update());
				}
			}, this.updateInterval * 5);  // Interval is extended to lower the amount of error messages
			return;
		}

		// Getting confirmation from user
		if (!confirmationFromUser(
			this.checker.getCurrentVersionTagName(),
			this.checker.getLatestVersionTagName()
		)) {
			LOGGER.info("User decided not to update");
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> update());
				}
			}, this.updateInterval * 5);  // Longer wait time since they denied it
			return;
		}
		LOGGER.info("User decided to update");

		Process updateInitializer;
		try {
			ProcessBuilder updateInitializerBuilder = new ProcessBuilder();
			updateInitializerBuilder.command(UPDATE_INITIALIZER_LAUNCH_SCRIPT_FILE_NAME, updatePackageDownloadUrl, "src.zip", String.valueOf(ProcessHandle.current().pid()));
			updateInitializerBuilder.inheritIO();
			updateInitializer = updateInitializerBuilder.start();
		} catch (IOException e) {
			LOGGER.error("Error launching update handler", e);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> update());
				}
			}, this.updateInterval);
			return;
		}
		System.exit(0);
	}

	public static void forceCheckUpdate() {
		LOGGER.info("User is checking for updates manually");
		try {
			if (!singleton.checker.newerReleaseAvailable()) {
				LOGGER.info("No updates available");
				Alert noUpdate = new Alert(
					Alert.AlertType.INFORMATION,
					"Нет обновлений приложения. Если у вас возникли проблемы обратитесь к разработчику в пункте меню \"Помощь\" -> \"Связь с разработчиком\""
				);
				noUpdate.showAndWait();
			} else {
				singleton.update();
			}
		} catch (IOException e) {
			LOGGER.error("Could not determine newer release availability");
		}
	}

	/**
	 * Выдаёт пользователю окно с выбором "Да" или "Нет" надо ли обновлять приложение или нет.
	 * @return
	 * false - если пользователь выбрал "Нет" или закрыл окно;
	 * true - если пользователь выбрал "Да".
	 */
	private boolean confirmationFromUser(String oldVersionTagName, String newVersionTagName) {
		Alert updateOrNo = new Alert(
			Alert.AlertType.CONFIRMATION,
				String.format(
					"Доступно новое обновление: с версии \"%s\" до \"%s\". Установить обновление? Приложение будет закрыто во время обновления и откроется заново по его завершению.",
					oldVersionTagName, newVersionTagName
				)
		);
		Optional<ButtonType> result = updateOrNo.showAndWait();
		return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
	}

	/**
	 * Creates the update manager singleton instance to manage all updates.
	 * @param args:
	 * args[0] - Number of minutes to wait between updating information about releases;
	 * args[1] - Number of minutes to wait between checking for newer version and downloading it.
	 * @return Run codes:
	 * 0 - launched successfully;
	 * 1 - manager is already on;
	 * 2 - invalid argument count;
	 * 3 - invalid argument for infoInterval;
	 * 4 - invalid argument for updateInterval.
	 */
	public static int launch(String[] args) {
		if (singleton != null) {
			LOGGER.warn("Update manager is already running");
			return 1;
		}

		if (args.length != 2) {
			LOGGER.error(String.format("Invalid argument count: %s", args.length));
			return 2;
		}

		long infoIntervalMinutes;
		try {
			infoIntervalMinutes = Long.parseLong(args[0]);
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Can't convert info interval minutes to Long: \"%s\"", args[0]));
			return 3;
		}

		long updateIntervalMinutes;
		try {
			updateIntervalMinutes = Long.parseLong(args[1]);
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Can't convert update interval minutes to Long: \"%s\"", args[1]));
			return 4;
		}

		singleton = new UpdatesManager(  // Operations bellow is to convert to milliseconds
			infoIntervalMinutes * 60 * 1000,
			updateIntervalMinutes * 60 * 1000
		);
		LOGGER.info("Update manager launched successfully");
		return 0;
	}
}
