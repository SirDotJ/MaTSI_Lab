package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class UpdateHandlerLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module Updater=UpdateHandler>
				Log content start:
				""";
	}
}