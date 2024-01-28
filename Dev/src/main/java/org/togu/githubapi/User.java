package org.togu.githubapi;

import org.json.JSONObject;

// Describes a single user on github and its info
public class User {
	public final UserInfo info;
	public User(JSONObject jsonUploaderInfo) {
		this.info = new UserInfo(
			jsonUploaderInfo.get("login").toString(),
			jsonUploaderInfo.get("id").toString(),
			jsonUploaderInfo.get("node_id").toString(),
			jsonUploaderInfo.get("avatar_url").toString(),
			jsonUploaderInfo.get("gravatar_id").toString(),
			jsonUploaderInfo.get("url").toString(),
			jsonUploaderInfo.get("html_url").toString(),
			jsonUploaderInfo.get("followers_url").toString(),
			jsonUploaderInfo.get("following_url").toString(),
			jsonUploaderInfo.get("gists_url").toString(),
			jsonUploaderInfo.get("starred_url").toString(),
			jsonUploaderInfo.get("subscriptions_url").toString(),
			jsonUploaderInfo.get("organizations_url").toString(),
			jsonUploaderInfo.get("repos_url").toString(),
			jsonUploaderInfo.get("events_url").toString(),
			jsonUploaderInfo.get("received_events_url").toString(),
			jsonUploaderInfo.get("type").toString(),
			Boolean.parseBoolean(jsonUploaderInfo.get("site_admin").toString())
		);
	}
}


