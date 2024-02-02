package org.togu.githubautomation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togu.githubapi.Release;

import java.io.IOException;

public class CreateRelease {
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateRelease.class);

	public static void main(String[] args) {
		CurrentRelease release = new CurrentRelease(true);
		ProjectRepository repository = new ProjectRepository();
		Release newRelease;
		try {
			LOGGER.info("Creating new release...");
			newRelease = repository.createRelease(release.postInfo, true);
			LOGGER.info("Uploading release assets...");
			repository.uploadReleaseAssets(release.assets, newRelease);
			CurrentRelease finalRelease = new CurrentRelease(false);
			repository.updateRelease(finalRelease.postInfo);
		} catch (IOException e) {
			LOGGER.error("Failed to create project release", e);
			return;
		}
		LOGGER.info(String.format("Successfully created release under tag name: \"%s\", link to release: \"%s\"", newRelease.info.tag_name(), newRelease.info.url()));
	}
}
