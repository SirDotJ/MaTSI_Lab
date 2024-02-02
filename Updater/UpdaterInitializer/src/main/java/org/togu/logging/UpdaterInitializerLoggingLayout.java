package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class UpdaterInitializerLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module Updater=UpdaterInitializer>
				Log content start:
				""";
	}
}
