package org.togu;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Updater {
	private static final Logger LOGGER = LoggerFactory.getLogger(Updater.class);

	// Pulls latest release and it's contents
	private static final String LATEST_RELEASE_API_CALL = "https://api.github.com/repos/SirDotJ/MaTSI_Lab/releases/latest";
	private static final String MAIN_COMPONENT = "main_agent_name";

	private static void downloadLatestAsset(String assetName, String extension) throws IllegalArgumentException {
		String targetName = MetaInfo.getComponentInfo(assetName) + extension;

		JSONObject latestRelease = FileParser.readURLToJSONObject(LATEST_RELEASE_API_CALL);
		JSONArray latestReleaseAssets = latestRelease.getJSONArray("assets");
		for (int i = 0; i < latestReleaseAssets.length(); i++) {
			JSONObject asset = latestReleaseAssets.getJSONObject(i);
			String providedLink = asset.get("browser_download_url").toString();
			if (providedLink.contains(targetName)) {
				try {
					URL downloadLink = new URL(providedLink);
					ReadableByteChannel byteChannel = Channels.newChannel(downloadLink.openStream());
					FileOutputStream fileOutputStream = new FileOutputStream(targetName);
					fileOutputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
					fileOutputStream.close();
					return;
				} catch (IOException e) {
					LOGGER.error("IOException occurred while trying to download and save found file", e);
					throw new RuntimeException("IOException occurred");
				}
			}
		}
		LOGGER.error(String.format("No component named \"%s\" found", assetName));
		throw new IllegalArgumentException("No component under provided name found in latest release");
	}

	public static void main(String[] args) {
		downloadLatestAsset(MAIN_COMPONENT, ".jar");
	}
}
