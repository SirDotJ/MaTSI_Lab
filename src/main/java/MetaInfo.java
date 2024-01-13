import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public class MetaInfo {
	private static final Logger LOGGER = LoggerFactory.getLogger(MetaInfo.class);

	public static MetaInfo singleton = null;
	private static final String APPLICATION_INFO_JSON_RELATIVE_PATH = "./meta/applicationInfo.json";
	private static final String COMPONENT_INFO_JSON_RELATIVE_PATH = "./meta/componentsInfo.json";

	private final JSONObject APPLICATION_INFO;
	private final JSONObject COMPONENT_INFO;

	private MetaInfo() {
		// APPLICATION_INFO
		String applicationInfoAbsolutePath;
		applicationInfoAbsolutePath = Paths.get(new File(APPLICATION_INFO_JSON_RELATIVE_PATH).toURI()).toAbsolutePath().toString();
		APPLICATION_INFO = JSONParser.readFile(applicationInfoAbsolutePath);

		// COMPONENT_INFO}
		String componentInfoAbsolutePath;
		componentInfoAbsolutePath = Paths.get(new File(COMPONENT_INFO_JSON_RELATIVE_PATH).toURI()).toAbsolutePath().toString();
		COMPONENT_INFO = JSONParser.readFile(componentInfoAbsolutePath);
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
}
