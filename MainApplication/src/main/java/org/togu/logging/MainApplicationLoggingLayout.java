package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class MainApplicationLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module MainApplication>
				Log content start:
				""";
	}
}
