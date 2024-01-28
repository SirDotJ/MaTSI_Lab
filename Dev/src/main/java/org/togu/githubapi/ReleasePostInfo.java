package org.togu.githubapi;

import okhttp3.MediaType;
import okhttp3.RequestBody;

// Contains information to be used for making a post request to Github releases section of repository
public record ReleasePostInfo(
	String authorization_token,  // not included in post body
	String tag_name,
	String name,
	String body,
	Boolean draft,
	Boolean prerelease,
	Boolean generate_release_notes
) {
	public RequestBody toPostBody() {
		return RequestBody.create(this.toJson(), MediaType.get("application/json; charset=utf-8"));
	}
	public String toJson() {
		return String.format(
			"{" +
				"\"tag_name\": \"%s\"," +
				"\"name\": \"%s\"," +
				"\"body\": \"%s\"," +
				"\"draft\": %s," +
				"\"prerelease\": %s," +
				"\"generate_release_notes\": %s" +
			"}", this.tag_name, this.name, this.body, this.draft, this.prerelease, this.generate_release_notes
		);
	}
}
