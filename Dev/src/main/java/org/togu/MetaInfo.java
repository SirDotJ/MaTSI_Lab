package org.togu;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaInfo.class);

	static {  // Used to specify logback.xml file to be used: one in the resource path, and not in modules this module depends on
		System.setProperty("logback.configurationFile", "logback.xml");
	}

	public static MetaInfo singleton = null;
	private static final String APPLICATION_INFO_JSON_RELATIVE_PATH = "meta/applicationInfo.json";
	private static final String COMPONENT_INFO_JSON_RELATIVE_PATH = "meta/componentsInfo.json";
	private static final String API_INFO_JSON_RELATIVE_PATH = "meta/apiInfo.json";

	private final JSONObject APPLICATION_INFO;
	private final JSONObject COMPONENT_INFO;
	private final JSONObject API_INFO;

	private MetaInfo() {
		// APPLICATION_INFO
		APPLICATION_INFO = FileParser.readResourceFileToJSONObject(APPLICATION_INFO_JSON_RELATIVE_PATH);

		// COMPONENT_INFO}
		COMPONENT_INFO = FileParser.readResourceFileToJSONObject(COMPONENT_INFO_JSON_RELATIVE_PATH);

		// API_INFO
		API_INFO = FileParser.readResourceFileToJSONObject(API_INFO_JSON_RELATIVE_PATH);
	}

	public static String getApplicationInfo(String key) {
		if (singleton == null) {
			singleton = new MetaInfo();
		}

		return singleton.APPLICATION_INFO.get(key).toString();
	}

	public static String getComponentInfo(String key) {
		if (singleton == null) {
			singleton = new MetaInfo();
		}

		return singleton.COMPONENT_INFO.get(key).toString();
	}

	public static String getAPIInfo(String api, String key) {
		if (singleton == null) {
			singleton = new MetaInfo();
		}

		return singleton.API_INFO.getJSONObject(api).get(key).toString();
	}
}
