package org.togu.updates;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togu.githubapi.Asset;
import org.togu.githubapi.Release;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Keeps the current and latest repository information and updates them if it sees any changes.
 * Used to check current state to see if CURRENT release is different from LATEST.
 */
public class UpdateChecker {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateChecker.class);
	private final long checkInterval; // in Milliseconds
	private Release current = null;
	private Release latest = null;

	/**
	 *
	 * @param checkInterval time in between checking releases in milliseconds
	 */
	UpdateChecker(long checkInterval) {
		this.checkInterval = checkInterval;
		this.update();
	}

	/**
	 * Method gets current and latest release information on a timer once it is called.
	 * Current does not get changed after getting it the first time.
	 * Latest may change if repository's latest release is changed.
	 * Any exceptions during runtime are ignored but logged.
	 */
	private void update() {
		if (this.current == null)  // Current
			try {
				this.current = ReleaseFetcher.fetchCurrentRelease();
			} catch (IOException e) {
				LOGGER.warn("Failed to get current release", e);
			}

		try {  // Latest
			this.latest = ReleaseFetcher.fetchLatestRelease();
		} catch (IOException e) {
			LOGGER.warn("Failed to get latest release", e);
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> update());
			}
		}, this.checkInterval);
	}

	/**
	 * Method checks if current release is different from latest.
	 * Check occurs using id since any latest release that is different from current will have a different id.
	 * Same logic applies if current marked release is actually not the latest after first check.
	 * @return
	 * - false - if current is same as latest (i.e. No changes were made since launching application);
	 * - true - if there is a newer version available.
	 * @throws IllegalStateException Occurs if either of the releases is null, which indicates that it was never received from Repository.
	 */
	boolean newerReleaseAvailable() throws IllegalStateException {
		try {
			this.latest = ReleaseFetcher.fetchLatestRelease();
		} catch (IOException e) {
			LOGGER.warn("Failed to get latest release", e);
		}

		if (this.current == null || this.latest == null) {
			LOGGER.warn("Can't compare releases since current or latest is null. They were not received yet.");
			throw new IllegalStateException("Current or latest release was never received");
		}
		return !Objects.equals(
			this.current.info.id(),
			this.latest.info.id()
		);
	}

	private static final String UPDATE_PACKAGE_NAME = "src.zip";

	/**
	 * Method returns a download link to get the update package for an upgrade from current to latest.
	 * @return String url to download src.zip file with updated src folder contents.
	 * @throws IllegalStateException Occurs if somehow latest release was never received.
	 * @throws FileNotFoundException Occurs if update package under saved name was not found in the latest release.
	 */
	String getLatestDownloadLink() throws IllegalStateException, FileNotFoundException {
		if (latest == null) {
			LOGGER.error("Latest release was never received before calling this method");
			throw new IllegalStateException("No latest release saved");
		}
		for (Asset asset : latest.info.assets()) {
			if (asset.info.name().equals(UPDATE_PACKAGE_NAME))
				return asset.info.browser_download_url();
		}
		LOGGER.error(String.format("No update package named: \"%s\" found in latest release", UPDATE_PACKAGE_NAME));
		throw new FileNotFoundException("Update package under saved name is not found in latest release");
	}

	String getCurrentVersionTagName() {
		if (this.current == null)
			return "???";
		return this.current.info.tag_name();
	}
	String getLatestVersionTagName() {
		if (this.latest == null)
			return "???";
		return this.latest.info.tag_name();
	}
}
