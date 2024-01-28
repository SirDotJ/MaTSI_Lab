package org.togu.githubapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Release {
	public final ReleaseInfo info;
	public Release(JSONObject jsonInfo) {
		String discussions_url; // discussion_url doesn't always exist
		try {
			discussions_url = jsonInfo.get("discussion_url").toString();
		} catch (JSONException e) {
			discussions_url = "";
		}
		this.info = new ReleaseInfo(
			jsonInfo.get("url").toString(),
			jsonInfo.get("html_url").toString(),
			jsonInfo.get("assets_url").toString(),
			jsonInfo.get("upload_url").toString(),
			jsonInfo.get("tarball_url").toString(),
			jsonInfo.get("zipball_url").toString(),
			discussions_url,
			jsonInfo.get("id").toString(),
			jsonInfo.get("node_id").toString(),
			jsonInfo.get("tag_name").toString(),
			jsonInfo.get("target_commitish").toString(),
			jsonInfo.get("name").toString(),
			jsonInfo.get("body").toString(),
			Boolean.parseBoolean(jsonInfo.get("draft").toString()),
			Boolean.parseBoolean(jsonInfo.get("prerelease").toString()),
			jsonInfo.get("created_at").toString(),
			jsonInfo.get("published_at").toString(),
			new User(jsonInfo.getJSONObject("author")),
			parseAssets(jsonInfo.getJSONArray("assets"))
		);
	}
	private List<Asset> parseAssets(JSONArray jsonArrayAssets) {
		List<Asset> assets = new ArrayList<>();
		jsonArrayAssets.forEach(asset -> assets.add(new Asset(this, (JSONObject) asset)));
		return assets;
	}
}
