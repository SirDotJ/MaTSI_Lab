import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONParser {
	private static final Charset USED_CHARSET = StandardCharsets.UTF_8;
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONParser.class);

	public static JSONObject readURL(String url) {
		InputStream inputStream;
		try {
			inputStream = new URL(url).openStream();
		} catch (IOException e) {
			LOGGER.error("During attempt to openStream on passed URL", e);
			throw new IllegalArgumentException("Passed URL led to exception: " + e);
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, USED_CHARSET));
		String jsonText = readBufferedReader(bufferedReader);
		return new JSONObject(jsonText);
	}

	public static JSONObject readFile(String filePath) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			LOGGER.error(String.format("During attempt to readFile did not find file under path: \"%s\"", filePath), e);
			throw new IllegalArgumentException("No file found under provided path");
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, USED_CHARSET));
		String jsonText = readBufferedReader(bufferedReader);
		return new JSONObject(jsonText);
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
