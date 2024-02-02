package org.togu;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class UpdateInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateInitializer.class);

	private static final String UPDATE_HANDLER_FILE_NAME = "UpdateHandler.jar";
	private static final String UPDATE_HANDLER_START_SCRIPT_FILE_NAME = "start_update_handler.bat";

	/**
	 * Downloads the update package then unzips and launches UpdateHandler.jar.
	 * @param args:
	 * args[0] - download link to get the update package;
	 * args[1] - name of update package;
	 * args[2] - pid of previous process which has to end before doing anything.
	 * Exit codes:
	 * 0 - update initialized successfully and downloaded UpdateHandler has started;
	 * 1 - invalid amount of parameters were passed as arguments;
	 * 2 - passed pid argument cannot be converted to long;
	 * 3 - failed to download the update package from last release;
	 * 4 - failed to extract update handler from update package;
	 * 5 - failed to start extracted update handler.
	 */
	public static void main(String[] args) {
		LOGGER.info("Starting update initializer");

		if (args.length != 3) {
			LOGGER.error("Invalid amount of parameters passed. 3 Are required.");
			System.exit(1);
			return;
		}

		String urlDownloadLink = args[0];
		String updatePackage = args[1];

		long pid;
		try {
			pid = Long.parseLong(args[2]);
		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Can't convert provided pid to Long: \"%s\"", args[1]));
			System.exit(2);
			return;
		}

		try {
			downloadFile(urlDownloadLink, updatePackage);
		} catch (IOException e) {
			LOGGER.error(String.format("Failed to download file at url: \"%s\"", urlDownloadLink));
			System.exit(3);
			return;
		}

		try {
			extractUpdateHandler(updatePackage, UPDATE_HANDLER_FILE_NAME);
		} catch (IOException e) {
			LOGGER.error(String.format("Failed to extract file named: \"%s\" from zip file: \"%s\"", UPDATE_HANDLER_FILE_NAME, updatePackage));
			System.exit(4);
			return;
		}

		waitForProcessToEnd(pid);

		try {
			startUpdateHandler(UPDATE_HANDLER_START_SCRIPT_FILE_NAME);
		} catch (IOException e) {
			LOGGER.error("Couldn't start update handler");
			System.exit(5);
			return;
		}
		LOGGER.info("Update initializer ended successfully");
		System.exit(0);
	}

	private static void downloadFile(String packageDownloadUrl, String updatePackageName) throws IOException {
		LOGGER.info(String.format("Starting to download update package from url: \"%s\"", packageDownloadUrl));
		Request request = new Request.Builder()
			.url(packageDownloadUrl)
			.build();
		Response response;
		try {
			response = new OkHttpClient().newCall(request).execute();
		} catch (IOException e) {
			LOGGER.error("Couldn't download update package file", e);
			throw new IOException("Couldn't download update package file", e);
		}
		ResponseBody responseBody = response.body();
		if (responseBody == null) {
			LOGGER.error(String.format("Request responded with empty body, make sure the following link is up and running: \"%s\"", packageDownloadUrl));
			throw new IOException("Get request responded with empty body");
		}

		InputStream is = responseBody.byteStream();

		BufferedInputStream input = new BufferedInputStream(is);
		OutputStream output;
		try {
			output = new FileOutputStream(updatePackageName);
		} catch (FileNotFoundException e) {
			LOGGER.error(String.format("Invalid output file name: \"%s\"", updatePackageName));
			throw new IOException("Invalid output file name");
		} catch (SecurityException e) {
			LOGGER.error(String.format("System denies read access to file: \"%s\"", updatePackageName));
			throw new IOException("System denied access");
		}

		byte[] data = new byte[1024];
		int count;
		while ((count = input.read(data)) != -1)
			output.write(data, 0, count);

		try {
			output.flush();
		} catch (IOException e) {
			LOGGER.warn("IOException while trying to flush output");
		}

		try {
			output.close();
		} catch (IOException e) {
			LOGGER.warn("IOException while trying to close output");
		}

		try {
			input.close();
		} catch (IOException e) {
			LOGGER.warn("IOException while trying to close input");
		}

		LOGGER.info("Update package downloaded successfully");
	}

	/**
	 * Method searches provided file zipFileName for entry named handlerFileName and extracts it into the same folder as the zip file
	 * @param zipFileName Zip file to search for the file
	 * @param handlerFileName File to search for in the zip file
	 * @throws IOException Occurs if anything wrong happens during the extraction process
	 */
	private static void extractUpdateHandler(String zipFileName, String handlerFileName) throws IOException {
		LOGGER.info(String.format("Starting to extract file: \"%s\" from zip file: \"%s\"", handlerFileName, zipFileName));

		// Opening zip file
		File zipFile = new File(zipFileName);
		if (!zipFile.exists()) {
			LOGGER.error(String.format("File under name: \"%s\" doesn't exist", zipFileName));
			throw new IOException("No zip file under provided name exists");
		}
		ZipInputStream zis;
		try {
			zis = new ZipInputStream(
				new FileInputStream(zipFile)
			);
		} catch (FileNotFoundException e) {
			LOGGER.error(String.format("Provided file \"%s\" doesn't exist to make FileInputStream", zipFile.getName()), e);
			throw new IOException("No zip file exists when making FileInputStream");
		} catch (SecurityException e) {
			LOGGER.error(String.format("System denied read access to file: \"%s\"", zipFile.getName()), e);
			throw new IOException("No read access to provided file");
		}

		// Searching zip file for our file
		ZipEntry zipEntry;
		do {
			try {
				zipEntry = zis.getNextEntry();
			} catch (ZipException e) {
				LOGGER.error("ZipException occurred while getting next zip entry", e);
				throw new IOException("ZipException occurred during search", e);
			} catch (IOException e) {
				LOGGER.error("IOException occurred while getting next zip entry", e);
				throw new IOException("IOException occurred during search", e);
			}

			String fileName;
			try {
				fileName = zipEntry.getName();
			} catch (NullPointerException e) {
				LOGGER.error("No file name found for received zip entry", e);
				throw new IOException("No file name for this zip entry", e);
			}

			if (Objects.equals(fileName, handlerFileName))
				break;
		} while (zipEntry != null);
		if (zipEntry == null) {
			LOGGER.error(String.format("No file named: \"%s\" found in zip file: \"%s\"", UPDATE_HANDLER_FILE_NAME, zipFile.getName()));
			throw new IOException("No update handler found in zip file");
		}

		// Writing found file from zip file into local
		File updateHandler = new File(zipEntry.getName());
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(updateHandler);
		} catch (FileNotFoundException e) {
			LOGGER.error(String.format("File named \"%s\" cannot be opened for output", updateHandler.getName()), e);
			throw new IOException("Can't open file for output before extraction");
		} catch (SecurityException e) {
			LOGGER.error(String.format("System denied access to file named \"%s\" for output", updateHandler.getName()), e);
			throw new IOException("System denied access to file for output before extraction");
		}
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			LOGGER.error(String.format("IOException occurred while extracting file: \"%s\" from zip file: \"%s\"", UPDATE_HANDLER_FILE_NAME, zipFile.getName()), e);
			throw new IOException("IOException occurred during extraction process", e);
		}

		// Closing opened streams
		try {
			fos.close();
		} catch (IOException e) {
			LOGGER.warn(String.format("Can't close FileOutputStream for file: \"%s\"", updateHandler.getName()));
		}
		try {
			zis.closeEntry();
		} catch (ZipException e) {
			LOGGER.warn(String.format("Zip error has occurred when trying to close zip file \"%s\"'s entry: \"%s\"", zipFile.getName(), zipEntry.getName()), e);
		} catch (IOException e) {
			LOGGER.warn(String.format("IOException has occurred when trying to close zip file: \"%s\"'s entry: \"%s\"", zipFile.getName(), zipEntry.getName()), e);
		}
		try {
			zis.close();
		} catch (IOException e) {
			LOGGER.warn(String.format("IOException when trying to close zip file: \"%s\"", zipFile.getName()));
		}

		LOGGER.info(String.format("File: \"%s\" extracted from zip file: \"%s\" successfully", handlerFileName, zipFileName));
	}

	private static void startUpdateHandler(String startScript) throws IOException {
		LOGGER.info(String.format("Starting update handler using script: \"%s\"", startScript));
		Process updateHandler;
		try {
			ProcessBuilder updateHandlerBuilder = new ProcessBuilder();
			updateHandlerBuilder.command(startScript, String.valueOf(ProcessHandle.current().pid()), "src.zip", UPDATE_HANDLER_FILE_NAME);
			updateHandlerBuilder.inheritIO();
			updateHandler = updateHandlerBuilder.start();
		} catch (IOException e) {
			LOGGER.error("Error starting UpdateHandler", e);
			throw new IOException("Couldn't start UpdateHandler", e);
		}
		LOGGER.info("Update handler started successfully");
	}

	private static void waitForProcessToEnd(long pid) {
		LOGGER.info(String.format("Waiting for main process under pid %s to end", pid));
		Optional<ProcessHandle> mainApplication = ProcessHandle.of(pid);
		if (mainApplication.isEmpty()) {
			LOGGER.warn("No main application process detected, is it already over?");
		} else {
			ProcessHandle mainApplicationHandle = mainApplication.get();
			do {
				try {
					Thread.sleep(10);
				} catch (InterruptedException ignore) {
				}
			} while (mainApplicationHandle.isAlive());
		}
		LOGGER.info("Main process has ended");
	}
}
