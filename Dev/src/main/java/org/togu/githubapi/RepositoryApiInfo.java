package org.togu.githubapi;

public record RepositoryApiInfo(
	String owner,
	String repo,
	String authorizationToken
) {
	public String baseUrl() {
		return String.format("https://api.github.com/repos/%s/%s", this.owner, this.repo);
	}
	public String releases() {
		return String.format("%s/releases", this.baseUrl());
	}
	public String latestRelease() { return String.format("%s/latest", this.releases()); }
	public String releaseFromId(int id) {
		return this.releaseFromId(String.valueOf(id));
	}
	public String releaseFromId(String id) {
		return String.format("%s/%s", this.releases(), id);
	}
	public String releaseFromTagName(String tagName) {
		return String.format("%s/tags/%s", this.releases(), tagName);
	}
	public String assets() { return String.format("%s/assets", this.baseUrl()); }
	public String assetFromId(int id) { return this.assetFromId(String.valueOf(id)); }
	public String assetFromId(String id) { return String.format("%s/%s", this.assets(), id); }
	public String assetUploadFromReleaseId(String id, String fileName) {
		return String.format("https://uploads.github.com/repos/%s/%s/releases/%s/assets?name=%s",
			this.owner,
			this.repo,
			id,
			fileName
		);
	}
}
