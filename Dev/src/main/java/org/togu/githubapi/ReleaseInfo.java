package org.togu.githubapi;

import java.util.List;

public record ReleaseInfo(
	String url,
	String html_url,
	String assets_url,
	String upload_url,
	String tarball_url,
	String zipball_url,
	String discussion_url,
	String id,
	String node_id,
	String tag_name,
	String target_commitish,
	String name,
	String body,
	Boolean draft,
	Boolean prerelease,
	String created_at,
	String published_at,
	User author,
	List<Asset> assets
) {
}
