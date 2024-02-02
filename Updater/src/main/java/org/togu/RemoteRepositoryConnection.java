package org.togu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.togu.githubapi.Release;
import org.togu.githubautomation.ProjectRepository;

import java.io.IOException;

public class RemoteRepositoryConnection {
	private static final Logger LOGGER = LoggerFactory.getLogger(RemoteRepositoryConnection.class);
	private static final ProjectRepository REPOSITORY = new ProjectRepository(AuthorizationAgent.getAuthorizationToken());
	public static VersionInformation getLatestVersionInformation() throws IOException {
		Release latestRelease;
		try {
			latestRelease = REPOSITORY.getLatestRelease();
		} catch (IOException e) {
			LOGGER.error("Error getting latest release", e);
			throw new IOException("Error getting latest release");
		}
		return new VersionInformation(
			latestRelease.info.tag_name(),
			latestRelease.info.id(),
			latestRelease.info.created_at(),
			latestRelease.info.published_at());
	}
	public static VersionInformation getAttachedVersionInformation() throws IOException {
		String tag_name = MetaInfo.getReleaseInfo("tag_name");
		Release attachedRelease;
		try {
			attachedRelease = REPOSITORY.getRelease("tag_name");
		} catch (IOException e) {
			LOGGER.error(String.format("Error getting release under tag name: \"%s\"", tag_name), e);
			throw new IOException("Error getting release using passed tag name");
		}
		return new VersionInformation(
			attachedRelease.info.tag_name(),
			attachedRelease.info.id(),
			attachedRelease.info.created_at(),
			attachedRelease.info.published_at()
		);
	}
}
