package org.togu.logging;

import ch.qos.logback.classic.PatternLayout;

public class CyberSecurityTrainingAppLoggingLayout extends PatternLayout {
	@Override
	public String getFileHeader() {
		return  """
				<traceback.log contents for module CyberSecurity Training App>
				Log content start:
				""";
	}
}
