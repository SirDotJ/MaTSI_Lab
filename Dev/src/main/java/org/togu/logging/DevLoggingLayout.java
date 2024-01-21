package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class DevLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module Dev>
				Log content start:
				""";
	}
}
