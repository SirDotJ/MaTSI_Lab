package org.togu.githubapi;

import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a connection to a remote repository hosted on GitHub.
 * Uses okhttp3 and org.json libraries when processing requests.
 * Uses slf4j and logback for logging purposes.
 * Currently supports operations to interact with repository's releases.
 */
public class RepositoryApi {
	private final static Logger LOGGER = LoggerFactory.getLogger(RepositoryApi.class);
	private final OkHttpClient client;
	public final RepositoryApiInfo info;

	/**
	 * Defines a connection to the repository using its owner, repo and authorization token.
	 * @param owner Username of the user the repository belongs to. Can be found in the URL of the repository.
	 * @param repo Name of the repository itself. Can be found in the URL of the repository.
	 * @param authorizationToken Token through which content type operations are authorized. Can be generated by the repository's owner in GitHub developer settings.
	 */
	public RepositoryApi(String owner, String repo, String authorizationToken) {
		this.info = new RepositoryApiInfo(
			owner,
			repo,
			authorizationToken
		);
		this.client = new OkHttpClient();
	}

	/**
	 * Checks if passed release exists.
	 * @param release Release to check for existence on the repository.
	 * @return
	 * - false: release doesn't exist or an error occurred while trying to get information about it;
	 * - true: release exists.
	 */
	public boolean releaseExists(Release release) {
		return this.releaseExists(release.info.tag_name());
	}

	/**
	 * Checks if passed release exists through its tag name.
	 * @param tagName Designated tag name of the release to get assets from.
	 * @return
	 * - false: release under the passed tag name doesn't exist or an error occurred while trying to get the release information;
	 * - true: release under the passed tag name exists.
	 */
	public boolean releaseExists(String tagName) {
		String url = this.info.releaseFromTagName(tagName);
		Request getRequest = this.getRequest(url);
		try {
			this.executeRequest(getRequest);
		} catch (IOException e) {
			LOGGER.warn("Exception occurred while checking for release existing", e);
			return false;
		}
		return true;
	}

	/**
	 * Returns latest release and its information.
	 * @return Latest release in a form of {@link org.togu.githubapi.Release} instance.
	 * @throws IOException Occurs if there was an error getting a release
	 */
	public Release getLatestRelease() throws IOException {
		String url = this.info.latestRelease();
		Request request = this.getRequest(url);

		JSONObject response;
		try {
			response = this.executeRequest(request);
		} catch (IOException | NullPointerException e) {
			LOGGER.error("No response", e);
			throw new IOException("No response");
		}

		return new Release(response);
	}

	/**
	 * Returns release and its information from repository through its tag name.
	 * @param tagName Designated tag name of the release to get assets from.
	 * @return Instance of {@link Release}.
	 * @throws IOException Occurs when passed release doesn't exist or when an error occurred while trying to access it.
	 */
	public Release getRelease(String tagName) throws IOException {
		if (!releaseExists(tagName)) {
			LOGGER.error(String.format("Release under tag name: \"%s\" could not be found", tagName));
			throw new IOException("Could not find release");
		}

		String url = this.info.releaseFromTagName(tagName);
		Request request = getRequest(url);
		JSONObject response;
		try {
			response = executeRequest(request);
		} catch (IOException e) {
			LOGGER.error("No response", e);
			throw new IOException("No response", e);
		}
		return new Release(response);
	}

	/**
	 * Used to define default options for creating a new release.
	 * Currently, force is set to true by default (see {@link #createRelease(ReleasePostInfo, boolean)} for more info).
	 * @param info Information to be used when making a new release (see {@link ReleasePostInfo} for more details).
	 * @return Instance of {@link Release} for newly created release.
	 * @throws IOException Occurs if there was an error when creating a new Release.
	 */
	public Release createRelease(ReleasePostInfo info) throws IOException {
		boolean forceByDefault = false;
		try {
			return this.createRelease(info, forceByDefault);
		} catch (IOException e) {
			LOGGER.error(String.format("Error creating release under tag name: \"%s\"", info.tag_name()), e);
			throw new IOException("Error creating release", e);
		}
	}

	/**
	 * Creates a new release using the provided information.
	 * @param info Information to be used when making a new release (see {@link ReleasePostInfo} for more details).
	 * @param force Defines if release should be overwritten if it already exists under the same tag name.
	 * @return Instance of {@link Release} for newly created release.
	 * @throws IOException Occurs if force is set to false and repository already exists or if there was an error when creating new release.
	 */
	public Release createRelease(ReleasePostInfo info, boolean force) throws IOException {
		if (releaseExists(info.tag_name())) {
			if (force) {
				deleteRelease(info.tag_name());
			} else {
				LOGGER.error(String.format("Release under tag name: \"%s\" already exists. Please try another tag name that is not taken", info.tag_name()));
				throw new IOException(String.format("Release under tag name: \"%s\" already exists", info.tag_name()));
			}
		}

		String url = this.info.releases();
		RequestBody data = info.toPostBody();
		Request postRequest = this.postRequest(url, data);
		JSONObject response;
		try {
			response = executeRequest(postRequest);
		} catch (IOException e) {
			LOGGER.error(String.format("Error posting release under tag name: \"%s\"", info.tag_name()), e);
			throw new IOException("Error posting release", e);
		}
		return new Release(response);
	}

	public Release updateRelease(ReleasePostInfo info) throws IOException {
		if (!releaseExists(info.tag_name())) {
			LOGGER.error(String.format("Release under tag name: \"%s\" doesn't exist.", info.tag_name()));
			throw new IOException("Provided release doesn't exist");
		}
		Release release = this.getRelease(info.tag_name());

		String url = this.info.releaseFromId(release.info.id());
		RequestBody data = info.toPostBody();
		Request patchRequest = this.patchRequest(url, data);
		JSONObject response;
		try {
			response = executeRequest(patchRequest);
		} catch (IOException e) {
			LOGGER.error(String.format("Error patching release under tag name: \"%s\"", info.tag_name()), e);
			throw new IOException("Error patching release", e);
		}
		return new Release(response);
	}

	/**
	 * Deletes release under provided tag name if it exists.
	 * @param tag_name Designated tag name of the repository to be deleted.
	 * @throws IOException Occurs if release failed to be deleted.
	 */
	public void deleteRelease(String tag_name) throws IOException {
		try {
			this.deleteRelease(this.getRelease(tag_name));
		} catch (IOException e) {
			LOGGER.error(String.format("Error deleting repository under tag name: \"%s\"", tag_name), e);
			throw new IOException("Error deleting release", e);
		}
	}

	/**
	 * Deletes provided release if it exists.
	 * @param release Release to delete.
	 * @throws IOException Occurs if release failed to be deleted.
	 */
	public void deleteRelease(Release release) throws IOException {
		if (!this.releaseExists(release))
			return;

		String url = String.format("%s/%s", this.info.releases(), release.info.id());
		Request deleteRequest = this.deleteRequest(url);
		try {
			this.executeRequest(deleteRequest);
		} catch (IOException e) {
			LOGGER.error(String.format("Error deleting release under tag name: \"%s\"", release.info.tag_name()), e);
			throw new IOException("Error deleting release", e);
		} catch (NullPointerException e) {
			LOGGER.warn(String.format("Tried to delete non-existent release under tag name: \"%s\"", release.info.tag_name()), e);
		}
	}

	/**
	 * Returns a list of all assets which are assigned to release under the passed tag name.
	 * @param tagName Designated tag name of the release to get assets from.
	 * @return List of all assets that are currently attached to the release.
	 * @throws IOException Occurs when passed release doesn't exist or when an error occurred while trying to access it.
	 */
	public List<Asset> getReleaseAssets(String tagName) throws IOException {
		if (!releaseExists(tagName)) {
			LOGGER.error(String.format("Release under tag name: \"%s\" could not be found", tagName));
			throw new IOException("Could not find release");
		}

		Release release;
		try {
			release = this.getRelease(tagName);
		} catch (IOException e) {
			LOGGER.error("No release.", e);
			throw new IOException("No release", e);
		}
		return release.info.assets();
	}

	/**
	 * Adds provided file to asset list in provided release if it's not already there. If it is then it deletes the old one before uploading new version.
	 * @param file File to be uploaded as asset.
	 * @param release Release to upload file to.
	 * @return Returned information about the posted asset.
	 * @throws IOException Occurs if one of the following occurs: couldn't check for duplicate asset, couldn't delete duplicate asset, couldn't upload new asset.
	 */
	public Asset uploadReleaseAsset(File file, Release release) throws IOException {
		List<Asset> existingAssets;
		try {
			existingAssets = this.getReleaseAssets(release.info.tag_name());
		} catch (IOException e) {
			LOGGER.error("Could not check release for existing assets", e);
			throw new IOException("Couldn't check release for existing assets", e);
		}

		for (Asset asset : existingAssets) {  // If asset is already in the release - remove it
			if (asset.info.name().equals(file.getName())) {
				if (!this.deleteReleaseAsset(asset)) {
					LOGGER.error("Duplicate asset could not be deleted");
					throw new IOException("Couldn't delete duplicate asset");
				} else
					break;
			}
		}

		String url = this.info.assetUploadFromReleaseId(release.info.id(), file.getName());
		RequestBody fileUpload = RequestBody.create(file, MediaType.parse("application/octet-stream"));
		Request postAssetRequest = this.postRequest(url, fileUpload);
		JSONObject response;
		try {
			response = this.executeRequest(postAssetRequest);
		} catch (IOException e) {
			LOGGER.error("Error uploading asset", e);
			throw new IOException("Error uploading asset", e);
		}
		return new Asset(release, response);
	}

	/**
	 * Uploads all provided files to passed release as assets.
	 * @param assets List of files to be uploaded as assets to release.
	 * @param release Release to upload files to.
	 * @return List of assets which were made after uploading provided files.
	 * @throws IOException Occurs if any of the files failed to be uploaded.
	 */
	public List<Asset> uploadReleaseAssets(List<File> assets, Release release) throws IOException {
		List<Asset> newAssets = new ArrayList<>();
		for (File asset : assets) {
			try {
				newAssets.add(this.uploadReleaseAsset(asset, release));
			} catch (IOException e) {
				LOGGER.error(String.format("Failed to upload asset under path: \"%s\"", asset.getPath()), e);
				throw new IOException("Failed to upload an asset", e);
			}
		}
		return newAssets;
	}

	/**
	 * Updates existing asset to have provided new name and label.
	 * @param asset Asset to be updated.
	 * @param name New name for the asset.
	 * @param label New label for the asset.
	 * @return Updated asset from GitHub api.
	 * @throws IOException Occurs if asset failed to update.
	 */
	public Asset updateReleaseAsset(Asset asset, String name, String label) throws IOException {
		String url = this.info.assetFromId(asset.info.id());
		RequestBody patchRequestBody = Asset.toPatchBody(name, label);
		Request postRequest = this.patchRequest(url, patchRequestBody);
		JSONObject response;
		try {
			response = this.executeRequest(postRequest);
		} catch (IOException e) {
			LOGGER.error("Error patching asset", e);
			throw new IOException("Error patching asset", e);
		}
		return new Asset(asset.parent, response);
	}


	/**
	 * Deletes an asset from repository through its id.
	 * @param asset Asset to be deleted.
	 * @return
	 * - false: asset was not deleted (asset doesn't exist or an error occurred);
	 * - true: asset was deleted successfully.
	 */
	public boolean deleteReleaseAsset(Asset asset) {
		String url = this.info.assetFromId(asset.info.id());
		Request deleteRequest = this.deleteRequest(url);
		try {
			this.executeRequest(deleteRequest);
		} catch (IOException e) {
			LOGGER.error("Error deleting asset", e);
			return false;
		}
		return true;
	}

	/**
	 * Deletes provided list of assets from repository.
	 * @param assetsToDelete List of assets to be deleted from the repository.
	 * @return
	 * - false: one of the assets failed to delete;
	 * - true: all assets were deleted successfully,
	 */
	public boolean deleteReleaseAssets(List<Asset> assetsToDelete) {
		List<Asset> assetsThatFailedToDelete = new ArrayList<>();
		for (Asset asset : assetsToDelete) {
			if (!deleteReleaseAsset(asset))
				assetsThatFailedToDelete.add(asset);
		}
		if (!assetsThatFailedToDelete.isEmpty()) {
			StringBuilder errorBuilder = new StringBuilder();
			errorBuilder.append("Failed to delete the following assets:");
			for (Asset asset : assetsThatFailedToDelete)
				errorBuilder.append(String.format("\n\tname: \"%s\", id: \"%s\", release: \"%s\"",
						asset.info.name(),
						asset.info.id(),
						asset.parent.info.tag_name())
				);
			LOGGER.error(errorBuilder.toString());
			return false;
		}
		return true;
	}

	private Request getRequest(String destination) {
		return new Request.Builder()
			.url(destination)
			.addHeader("Authorization", String.format("token %s", this.info.authorizationToken()))
			.addHeader("Accept", "application/vnd.github+json")
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}
	private Request postRequest(String destination, RequestBody data) {
		return new Request.Builder()
			.url(destination)
			.post(data)
			.addHeader("Authorization", String.format("token %s", this.info.authorizationToken()))
			.addHeader("Accept", "application/vnd.github+json")
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}
	private Request patchRequest(String destination, RequestBody data) {
		return new Request.Builder()
			.url(destination)
			.patch(data)
			.addHeader("Authorization", String.format("token %s", this.info.authorizationToken()))
			.addHeader("Accept", "application/vnd.github+json")
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}

	private Request deleteRequest(String destination) {
		return new Request.Builder()
			.url(destination)
			.delete()
			.addHeader("Authorization", String.format("token %s", this.info.authorizationToken()))
			.addHeader("Accept", "application/vnd.github+json")
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}
	private Request deleteRequest(String destination, RequestBody data) {
		return new Request.Builder()
			.url(destination)
			.delete(data)
			.addHeader("Authorization", String.format("token %s", this.info.authorizationToken()))
			.addHeader("Accept", "application/vnd.github+json")
			.addHeader("X-GitHub-Api-Version", "2022-11-28")
			.build();
	}
	private JSONObject executeRequest(Request request) throws IOException, NullPointerException {
		Response response;
		try {  // Make request
			response = this.client.newCall(request).execute();
		} catch (IOException e) {
			LOGGER.error("Received IOException while trying to process request");
			throw new IOException("Failed to make HTTP call", e);
		}
		if (!response.isSuccessful()) {  // Check if successful
			LOGGER.warn(String.format("HTTP response is unsuccessful. Error code: \"%s\". Message: \"%s\"", response.code(), response.message()));
			throw new IOException(String.format("HTTP response is unsuccessful. Error code: \"%s\"", response.code()));
		}
		String responseBodyContent;
		try {
			responseBodyContent = response.body().string();
		} catch (NullPointerException e) {
			LOGGER.warn("Accessing string of body threw NullPointerException", e);
			responseBodyContent = "";
		}
		if (responseBodyContent.equals("")) {
			LOGGER.warn(String.format("Empty response. Check if request is correct. Error code: \"%s\". Message: \"%s\"", response.code(), response.message()));
			throw new NullPointerException("Empty response");
		}
		response.body().close();
		return new JSONObject(responseBodyContent);
	}
}