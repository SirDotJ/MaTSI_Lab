package org.togu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RenameOutJarFiles {
	private static final Logger LOGGER = LoggerFactory.getLogger(RenameOutJarFiles.class);

	private static final String FULL_APPLICATION_WINDOWS_SRC_OUTPUT_PATH = "out/artifacts/FullApplication_Windows/src";
	private static final List<String> COMPONENT_FILE_NAMES = new ArrayList<>(Arrays.asList(
		"Launcher", "Trainer", "Updater"
	));
	private static final String VERSION_JSON_KEY = "version";
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	private static final String FILE_EXTENSION = ".jar";
	public static void main(String[] args) {
		for (String componentFileName : COMPONENT_FILE_NAMES) {
			String relativePathToFile = FULL_APPLICATION_WINDOWS_SRC_OUTPUT_PATH + "/" + componentFileName + FILE_EXTENSION;
			File file = new File(relativePathToFile);

			String version = MetaInfo.getApplicationInfo(VERSION_JSON_KEY);
			String currentTime = DATE_FORMAT.format(new Date());
			String newFileName = componentFileName + "_" + version + "_" + currentTime + FILE_EXTENSION;
			String newRelativePath = FULL_APPLICATION_WINDOWS_SRC_OUTPUT_PATH + "/" + newFileName;
			file.renameTo(new File(newRelativePath));
//			LOGGER.info(String.format("Old file name: \"%s\", new file name: \"%s\"", relativePathToFile, newRelativePath));
		}
	}
}
