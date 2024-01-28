package org.togu.githubapi;

// Describes a single uploaded asset to release and its properties
public record AssetInfo(
	String url,
	String browser_download_url,
	String id,
	String node_id,
	String name,
	String label,
	String state,
	String content_type,
	String size,
	String downloadCount,
	String created_at,
	String updated_at,
	User uploader
) {

}
