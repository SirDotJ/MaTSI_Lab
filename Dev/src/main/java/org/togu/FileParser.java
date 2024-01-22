package org.togu;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileParser {
	private static final Charset USED_CHARSET = StandardCharsets.UTF_8;
	private static final Logger LOGGER = LoggerFactory.getLogger(FileParser.class);

	public static JSONObject readURLToJSONObject(String url) {
		InputStream URLStream;
		try {
			URLStream = new URL(url).openStream();
		} catch (IOException e) {
			LOGGER.error("During attempt to openStream on passed URL", e);
			throw new IllegalArgumentException("Passed URL led to exception: " + e);
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(URLStream, USED_CHARSET));
		String jsonText = readBufferedReader(bufferedReader);
		return new JSONObject(jsonText);
	}

	public static JSONObject readLocalFileToJSONObject(String filePath) {
		InputStream fileStream;
		try {
			fileStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			LOGGER.error(String.format("During attempt to readFile did not find file under path: \"%s\"", filePath), e);
			throw new IllegalArgumentException("No file found under provided path");
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream, USED_CHARSET));
		String jsonText = readBufferedReader(bufferedReader);
		return new JSONObject(jsonText);
	}

	public static JSONObject readResourceFileToJSONObject(String jsonFileResourcePath) {
		InputStream resourceStream;
		try {
			resourceStream = FileParser.class.getClassLoader().getResourceAsStream(jsonFileResourcePath);
		} catch (NullPointerException e) {
			LOGGER.error(String.format("During attempt to read resource under path: \"%s\"", jsonFileResourcePath), e);
			throw new IllegalArgumentException("No file found under provided resource path");
		}
		if (resourceStream == null) {
			LOGGER.error(String.format("During attempt to read resource under path: \"%s\": result is null", jsonFileResourcePath));
			throw new IllegalArgumentException("No file found under provided resource path");
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceStream, USED_CHARSET));
		String jsonText = readBufferedReader(bufferedReader);
		return new JSONObject(jsonText);
	}

	public static File readLocalFile(String filePath) throws NullPointerException {
		return new File(filePath);
	}

	public static InputStream readResourceFile(String resourceFilePath) {
		return FileParser.class.getClassLoader().getResourceAsStream(resourceFilePath);
	}

	private static String readBufferedReader(BufferedReader reader) {
		StringBuilder builder = new StringBuilder();
        int letter;
		try {
			while ((letter = reader.read()) != -1) {
				builder.append((char) letter);
			}
		} catch (IOException e) {
			LOGGER.error("During attempt to read from bufferedReader", e);
			throw new RuntimeException("Exception occurred during attempt to read bufferedReader content: " + e);
		}
		return builder.toString();
	}
}
