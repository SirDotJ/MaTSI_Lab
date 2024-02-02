package org.togu;

public record VersionInformation(
	String tag_name,
	String id,
	String created_at,
	String published_at
) {
}
