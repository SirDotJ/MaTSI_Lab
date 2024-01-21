package org.togu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	public static void main(String[] args) {
		LOGGER.info("Here you go, info added");
		LOGGER.error("And this is an error");
		LOGGER.error("This is an error with exception", new RuntimeException("Hello, I am a problem!"));
	}
}