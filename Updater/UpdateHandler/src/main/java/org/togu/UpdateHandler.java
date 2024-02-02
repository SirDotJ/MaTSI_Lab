package org.togu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UpdateHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHandler.class);
	private static final String LAUNCHER_SCRIPT_FILE_NAME = "start_launcher.bat";

	/**
	 * Extracts everything from the existing update package into same folder (except for the program itself).
	 * @param args
	 * args[0] - pid of the update initializer process;
	 * args[1] - name of update package file to extract everything besides program itself from;
	 * args[2] - name of the file that this program is launched as (to ignore it when extracting zip file contents).
	 * Exit codes:
	 * 0 - update process ended successfully;
	 * 1 - invalid amount of parameters passed as arguments;
	 * 2 - pid format could not be converted to long;
	 * 3 - no update package was found when starting update handler.
	 */
	public static void main(String[] args) {
		LOGGER.info("Starting update handler");

		if (args.length != 3) {
			LOGGER.error("Invalid amount of parameters passed. 3 are required");
			System.exit(1);
			return;
		}

		long updateInitializerPid;
		try {
			updateInitializerPid = Long.parseLong(args[0]);
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Can't convert provided pid to Long: \"%s\"", args[0]));
			System.exit(2);
			return;
		}
		String updatePackageFileName = args[1];
		String updateHandlerFileName = args[2];

		waitForProcessToEnd(updateInitializerPid);

		// Unzip the rest of the files from the update package
		// looks unorganized, but it worked last time so not touching it
		File updatePackageFile = new File(updatePackageFileName);
		ZipInputStream zis;
		try{
			zis = new ZipInputStream(new FileInputStream(updatePackageFile));
		} catch (FileNotFoundException e) {
			LOGGER.error("No update package found on start, try again?");
			launchInitializer();
			System.exit(3);
			return;
		}
		ZipEntry zipEntry;
		try {
			zipEntry = zis.getNextEntry();
		} catch (IOException e) {
			LOGGER.error("IOException getting the first zip entry");
			return;
		}
		if (zipEntry == null) {
			LOGGER.error("First entry is null. Is the package empty?");
			return;
		}
		do {
			if (Objects.equals(zipEntry.getName(), updateHandlerFileName)) {
				try {
					zipEntry = zis.getNextEntry();
				} catch (IOException e) {
					LOGGER.error("IOException getting next zip entry");
				}
				continue;
			}
			File nextFile = new File(zipEntry.getName());
			FileOutputStream fos;
			byte[] buffer = new byte[1024];
			try {
				fos = new FileOutputStream(nextFile);
					int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				zis.closeEntry();
			} catch (IOException e) {
				LOGGER.error(String.format("IOException creating file output stream for file named: \"%s\"", zipEntry.getName()));
				return;
			}
			LOGGER.info(String.format("Unpacked file: \"%s\" successfully", nextFile.getName()));
			try {
				zipEntry = zis.getNextEntry();
			} catch (IOException e) {
				LOGGER.error("IOException getting next zip entry");
			}
		} while (zipEntry != null);
		try {
			zis.close();
		} catch (IOException e) {
			LOGGER.warn("IOException occurred trying to close zip file stream");
		}

		// Delete the used update package
		if (!updatePackageFile.delete())
			LOGGER.warn("No update package found when trying to delete it");
		else
			LOGGER.info("Update package deleted successfully");

		// Start the application again
		Process launcher;
		try {
			ProcessBuilder launcherBuilder = new ProcessBuilder();
			launcherBuilder.command(LAUNCHER_SCRIPT_FILE_NAME);
			launcherBuilder.inheritIO();
			launcher = launcherBuilder.start();
		} catch (IOException e) {
			LOGGER.error("IOException when starting the launcher again");
			return;
		}
		LOGGER.info("Update handler ended successfully");
		LOGGER.info("Update was a success");
		System.exit(0);
	}
	private static final String UPDATER_INITIALIZER_SCRIPT_FILE_NAME = "start_initialize_update.bat";
	private static void launchInitializer() {
		LOGGER.info("Launching update initializer");
		Process initializer;
		try {
			ProcessBuilder initializerBuilder = new ProcessBuilder();
			initializerBuilder.command(UPDATER_INITIALIZER_SCRIPT_FILE_NAME);
			initializerBuilder.inheritIO();
			initializer = initializerBuilder.start();
		} catch (IOException e) {
			LOGGER.error("IOException when starting update initializer again");
		}
		LOGGER.info("Launched update initializer successfully");
	}
	private static void waitForProcessToEnd(long pid) {
		LOGGER.info(String.format("Waiting for update initializer process under pid %s to end", pid));
		Optional<ProcessHandle> mainApplication = ProcessHandle.of(pid);
		if (mainApplication.isEmpty()) {
			LOGGER.warn("No update initializer process process detected, is it already over?");
		} else {
			ProcessHandle mainApplicationHandle = mainApplication.get();
			do {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignore) {
				}
			} while (mainApplicationHandle.isAlive());
		}
		LOGGER.info("Waiting for update initializer process has ended");
	}
}