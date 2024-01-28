package org.togu.githubapi;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONObject;

/**
 * Defines a single asset that is attached to the repository through a release
 */
public class Asset {
	/**
	 * Release to which the asset is attached to.
	 */
	public final Release parent;
	/**
	 * Contains information about the asset in a record form.
	 */
	public final AssetInfo info;

	/**
	 * Defines asset using release it comes from and information about it
	 * @param parent Release to which asset belongs to
	 * @param jsonInfo Information about the asset
	 */
	public Asset(Release parent, JSONObject jsonInfo) {
		this.parent = parent;
		this.info = new AssetInfo(
			jsonInfo.get("url").toString(),
			jsonInfo.get("browser_download_url").toString(),
			jsonInfo.get("id").toString(),
			jsonInfo.get("node_id").toString(),
			jsonInfo.get("name").toString(),
			jsonInfo.get("label").toString(),
			jsonInfo.get("state").toString(),
			jsonInfo.get("content_type").toString(),
			jsonInfo.get("size").toString(),
			jsonInfo.get("download_count").toString(),
			jsonInfo.get("created_at").toString(),
			jsonInfo.get("updated_at").toString(),
			new User(jsonInfo.getJSONObject("uploader"))
		);
	}
	public RequestBody toPatchBody() {
		return new FormBody.Builder()
			.add("name", this.info.name())
			.add("label", this.info.label())
			.build();
	}
	public static RequestBody toPatchBody(String name, String label) {
		return new FormBody.Builder()
			.add("name", name)
			.add("label", label)
			.build();
	}
}
