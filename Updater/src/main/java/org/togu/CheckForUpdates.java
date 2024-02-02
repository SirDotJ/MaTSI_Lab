package org.togu;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;

public class CheckForUpdates {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckForUpdates.class);
	private static VersionInformation CURRENT;
	static {
		try {
			CURRENT = RemoteRepositoryConnection.getAttachedVersionInformation();
		} catch (IOException e) {
			LOGGER.error("Couldn't check attached release on start");
			CURRENT = null;
		}
	}

	public static boolean newerVersionAvailable() throws IOException {
		if (CURRENT == null) {
			try {
				CURRENT = RemoteRepositoryConnection.getAttachedVersionInformation();
			} catch (IOException e) {
				LOGGER.error("Couldn't check attached release");
				throw new IOException("Can't check attached release");
			}
		}
		VersionInformation LATEST = RemoteRepositoryConnection.getLatestVersionInformation();
		Date current;
		Date latest;
		try {
			current = DateFormat.getDateInstance().parse(CURRENT.published_at());
			latest = DateFormat.getDateInstance().parse(LATEST.published_at());
		} catch (ParseException e) {
			LOGGER.error(String.format("Couldn't parse provided dates. Current: \"%s\". Latest: \"%s\"", CURRENT.published_at(), LATEST.published_at()));
			throw new IOException("Can't parse provided dates");
		}

		return !Objects.equals(LATEST.id(), CURRENT.id()) && (latest.after(current));
	}
}
