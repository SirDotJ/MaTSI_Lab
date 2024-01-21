package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class UpdaterLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module Updater>
				Log content start:
				""";
	}
}
