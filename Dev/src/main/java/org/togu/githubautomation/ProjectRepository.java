package org.togu.githubautomation;

import org.togu.MetaInfo;
import org.togu.githubapi.RepositoryApi;

public class ProjectRepository extends RepositoryApi {
	public ProjectRepository(String authorizationToken) {
		super(
			MetaInfo.getAPIInfo("github_project_repository", "owner"), // Owner
			MetaInfo.getAPIInfo("github_project_repository", "repo"), // Repo
			authorizationToken
		);
	}
	public ProjectRepository() {
		this(MetaInfo.getPrivateInfo("project_content_read_write_github_access_token"));
	}
}
