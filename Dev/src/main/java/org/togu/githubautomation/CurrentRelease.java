package org.togu.githubautomation;

import org.togu.FileParser;
import org.togu.MetaInfo;
import org.togu.githubapi.ReleasePostInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CurrentRelease {
	public final ReleasePostInfo postInfo;
	public final List<File> assets;
	public CurrentRelease(boolean preRelease) {
		this.postInfo = new ReleasePostInfo(
			MetaInfo.getPrivateInfo("project_content_read_write_github_access_token"), // Authorization token
			MetaInfo.getReleaseInfo("tag_name"), // Tag name
			MetaInfo.getReleaseInfo("name"), // Name
			getDescription(),
			Boolean.parseBoolean(MetaInfo.getReleaseInfo("draft")), // Draft
			preRelease || Boolean.parseBoolean(MetaInfo.getReleaseInfo("preRelease")), // Prerelease
			Boolean.parseBoolean(MetaInfo.getReleaseInfo("generate_release_notes")) // Generate release notes
		);
		this.assets = getAssets();
	}
	private static final String DESCRIPTION_FILE_PATH = "release/release_description.md";
	private static String getDescription() {
		String currentDescription = FileParser.getFileContents(DESCRIPTION_FILE_PATH);
		currentDescription = currentDescription.replace("\n", "<br/>"); // GitHub does not accept new lines in POST call
		currentDescription = currentDescription.replace("\"", "\\\""); // Quotes might break json parsing if not escaped
		currentDescription = currentDescription.replace("\r", "&nbsp;&nbsp;&nbsp;&nbsp;"); // Tabs are not a accepted in GitHub API
		currentDescription = currentDescription.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"); // Tabs are not a accepted in GitHub API
		return currentDescription;
	}
	private static List<File> getAssets() {
		List<File> assets = new ArrayList<>();
		List<String> assetPaths = MetaInfo.getReleaseAssetPaths();
		for (String assetPath : assetPaths) {
			assets.add(FileParser.readLocalFile(assetPath));
		}
		return assets;
	}
}
