package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class LauncherLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module Launcher>
				Log content start:
				""";
	}
}
