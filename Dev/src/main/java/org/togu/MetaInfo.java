package org.togu;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MetaInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaInfo.class);

	static {  // Used to specify logback.xml file to be used: one in the resource path, and not in modules this module depends on
		System.setProperty("logback.configurationFile", "logback.xml");
	}

	public static MetaInfo singleton = null;
	private static final String APPLICATION_INFO_JSON_RELATIVE_PATH = "meta/applicationInfo.json";
	private static final String COMPONENT_INFO_JSON_RELATIVE_PATH = "meta/componentsInfo.json";
	private static final String API_INFO_JSON_RELATIVE_PATH = "meta/apiInfo.json";
	private static final String PRIVATE_INFO_JSON_RELATIVE_PATH = "meta/privateInfo.json";
	private static final String RELEASE_INFO_JSON_RELATIVE_PATH = "release/release_info.json";

	private final JSONObject APPLICATION_INFO;
	private final JSONObject COMPONENT_INFO;
	private final JSONObject API_INFO;
	private final JSONObject PRIVATE_INFO;
	private final JSONObject RELEASE_INFO;

	private MetaInfo() {
		// APPLICATION_INFO
		APPLICATION_INFO = FileParser.readResourceFileToJSONObject(APPLICATION_INFO_JSON_RELATIVE_PATH);

		// COMPONENT_INFO}
		COMPONENT_INFO = FileParser.readResourceFileToJSONObject(COMPONENT_INFO_JSON_RELATIVE_PATH);

		// API_INFO
		API_INFO = FileParser.readResourceFileToJSONObject(API_INFO_JSON_RELATIVE_PATH);

		// PRIVATE_INFO (may be null outside of dev environment)
		JSONObject privateInfo;
		try {
			privateInfo = FileParser.readResourceFileToJSONObject(PRIVATE_INFO_JSON_RELATIVE_PATH);
		} catch (IllegalArgumentException e) {
			privateInfo = null;
		}
		PRIVATE_INFO = privateInfo;

		// RELEASE_INFO (may be null outside of dev environment)
		JSONObject releaseInfo;
		try {
			releaseInfo = FileParser.readResourceFileToJSONObject(RELEASE_INFO_JSON_RELATIVE_PATH);
		} catch (IllegalArgumentException e) {
			releaseInfo = null;
		}
		RELEASE_INFO = releaseInfo;
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
	// WARNING: this method should not be used outside of development environment due to confidentiality issues with the data
	public static String getPrivateInfo(String key) {
		if (singleton == null) {
			singleton = new MetaInfo();
		}

		if (singleton.PRIVATE_INFO == null)
			return "";

		return singleton.PRIVATE_INFO.get(key).toString();
	}

	public static String getReleaseInfo(String key) {
		if (singleton == null) {
			singleton = new MetaInfo();
		}

		if (singleton.RELEASE_INFO == null)
			return "";

		return singleton.RELEASE_INFO.get(key).toString();
	}
	public static List<String> getReleaseAssetPaths() {
		List<String> assetFilePaths = new ArrayList<>();
		JSONArray jsonArray = singleton.RELEASE_INFO.getJSONArray("assetPaths");
		jsonArray.forEach(path -> assetFilePaths.add(path.toString()));
		return assetFilePaths;
	}
}
