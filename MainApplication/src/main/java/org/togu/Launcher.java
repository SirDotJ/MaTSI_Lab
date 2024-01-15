package org.togu;

import java.io.IOException;

public class Launcher {
	public static void main(String[] args) {
		System.out.println("Downloading required Jar file...");
		Updater.main(null);
		System.out.println("Launching downloaded Jar file...");
		try {
			// Credit: https://stackoverflow.com/a/8496537
			Process process = Runtime.getRuntime().exec("jdk/bin/java -jar MaTSI_Lab.jar");
		} catch (IOException e) {
			System.out.println("Exception occurred! " + e);
			return;
		}
		System.out.println("Launch successful!");
	}
}
