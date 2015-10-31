package io.github.wreed12345;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class GameUpdater {
	/**
	 * GameUpdater.Java
	 * Checks if the games version is out dated / downloads new updates.
	 * Author: GabrielBailey74
	 */
	private static byte[] BYTE_BUFFER = new byte[1024];
	private String currentClientVersion;
	private String versionLocation;
	private String versionFileNameAndExt;
	private String gameName;
	private String mostRecentVersion;
	private boolean needsUpdating;
	private int downloadPercentage;
	private AtomicInteger downloadPercentageAtomic = new AtomicInteger();
	
	public GameUpdater(String currentClientVersion,
			String versionLocation, String versionFileNameAndExtension, String gameName)
			throws MalformedURLException, IOException {
		setCurrentClientVersion(currentClientVersion);
		setVersionLocation(versionLocation);
		setVersionFileNameAndExt(versionFileNameAndExtension);
		setGameName(gameName);
		setMostRecentVersion(getVersionFromUrl(versionLocation + versionFileNameAndExtension));
		System.out.println("Current client version: " + currentClientVersion);
		System.out.println("Newest Client Version: " + getMostRecentVersion());
		setNeedsUpdating(!currentClientVersion.equals(getMostRecentVersion()));
		System.out.println("Client needs updating: " + needsUpdating());

	}

	private File updateJar(String jarLocation, boolean printPercentage)
			throws MalformedURLException, IOException {
		return getJarFromURL(jarLocation,  getGameName() + getMostRecentVersion() + ".jar",
				printPercentage);
	}

	File downloadUpdatedJar(boolean printPercentage) throws MalformedURLException, IOException {
		return updateJar("game/" + getVersionLocation() + getGameName() + getMostRecentVersion() + ".jar",
				printPercentage);
	}

	public static String getVersionFromUrl(String versionFileURL) throws MalformedURLException,
			IOException {
		/*
		 * Initialize the Input Stream. (Downloading from remote system / server)
		 */
		BufferedInputStream in = new BufferedInputStream(new URL(versionFileURL).openStream());

		/*
		 * Initialize the Output Stream. (Downloading on local system / pc)
		 */
		File savedFile = new File("version.txt.tmp");
		savedFile.deleteOnExit();
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savedFile));

		/*
		 * Begin the download.
		 */
		int inCount;
		while ((inCount = in.read(BYTE_BUFFER, 0, BYTE_BUFFER.length)) != -1) {
			out.write(BYTE_BUFFER, 0, inCount);
		}

		/*
		 * Close the Input/Output streams.
		 */
		out.flush();
		out.close();
		in.close();

		// after the version data has been saved, read it and delete the .TMP file.
		String version = "";
		if (savedFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(savedFile));
			version = br.readLine();
			br.close();
			savedFile.delete();
		}
		return version;
	}

	private File getJarFromURL(String urlLocation, String saveLocation, boolean printPercentage)
			throws MalformedURLException, IOException {
		double numWritten = 0;
		double length = getURLSizeInKB(urlLocation);

		/*
		 * Initialize the Input Stream. (Downloading from remote system / server)
		 */
		urlLocation = urlLocation.replace("game/", "");
		BufferedInputStream in = new BufferedInputStream(new URL(urlLocation).openStream());

		File path = new File(System.getProperty("user.home") + File.separator + "Freeman");
		path.mkdir();
		
		File gameFile = new File(path, saveLocation);
		
		/*
		 * Initialize the Output Stream. (Downloading on local system / pc)
		 */
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(gameFile));

		/*
		 * Keeping track of when we started.
		 */
		long startTime = System.currentTimeMillis();

		/*
		 * Begin the download.
		 */
		int inCount;
		while ((inCount = in.read(BYTE_BUFFER, 0, BYTE_BUFFER.length)) != -1) {
			out.write(BYTE_BUFFER, 0, inCount);
			numWritten += inCount;

			/*
			 * Calculate the Percentage.
			 */
			setDownloadPercentage((int) (((double) numWritten / (double) length) * 100D));
			if (printPercentage) {
				System.out.println("Download is " + getDownloadPercentage() + "% complete, ");
			}

		}

		/*
		 * Close the Input/Output streams.
		 */
		out.flush();
		out.close();
		in.close();
		return gameFile;
	}

	/* Obtaining the files size in kilobytes */
	private Double getURLSizeInKB(String urlStr) {
		double contentLength = 0;
		try {
			urlStr = urlStr.replace("game/", "");
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			contentLength = httpConn.getContentLength();
			httpConn.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return contentLength;
	}

	boolean needsUpdating() {
		return needsUpdating;
	}

	public String getCurrentClientVersion() {
		return currentClientVersion;
	}

	public void setCurrentClientVersion(String currentClientVersion) {
		this.currentClientVersion = currentClientVersion;
	}

	public String getVersionLocation() {
		return versionLocation;
	}

	public void setVersionLocation(String versionLocation) {
		this.versionLocation = versionLocation;
	}

	public String getVersionFileNameAndExt() {
		return versionFileNameAndExt;
	}

	public void setVersionFileNameAndExt(String versionFileNameAndExt) {
		this.versionFileNameAndExt = versionFileNameAndExt;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setNeedsUpdating(boolean needsUpdating) {
		this.needsUpdating = needsUpdating;
	}

	public String getMostRecentVersion() {
		return mostRecentVersion;
	}

	public void setMostRecentVersion(String mostRecentVersion) {
		this.mostRecentVersion = mostRecentVersion;
	}

	public AtomicInteger getDownloadPercentage() {
		downloadPercentageAtomic.set(downloadPercentage);
		return downloadPercentageAtomic;
	}

	public void setDownloadPercentage(int downloadPercentage) {
		this.downloadPercentage = downloadPercentage;
	}

}