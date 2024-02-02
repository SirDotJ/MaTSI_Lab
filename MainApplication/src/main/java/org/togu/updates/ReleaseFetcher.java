package org.togu.updates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togu.MetaInfo;
import org.togu.githubapi.Release;
import org.togu.githubautomation.ProjectRepository;

import java.io.IOException;

/**
 * Returns releases from the project repository. Current one and latest one.
 */
public class ReleaseFetcher {
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateChecker.class);
	private static final ProjectRepository PROJECT_REPOSITORY = new ProjectRepository(AuthorizationAgent.getAuthorizationToken());
	static Release fetchCurrentRelease()throws IOException {
		String locallySavedTagName = MetaInfo.getReleaseInfo("tag_name");
		try {
			return PROJECT_REPOSITORY.getRelease(locallySavedTagName);
		} catch (IOException e) {
			LOGGER.warn("Failed to fetch current release, will try again later", e);
			throw new IOException("Failed to fetch current release");
		}
	}
	static Release fetchLatestRelease() throws IOException {
		try {
			return PROJECT_REPOSITORY.getLatestRelease();
		} catch (IOException e) {
			LOGGER.error("Failed to fetch latest release", e);
			throw new IOException("Failed to fetch latest release");
		}
	}
}
