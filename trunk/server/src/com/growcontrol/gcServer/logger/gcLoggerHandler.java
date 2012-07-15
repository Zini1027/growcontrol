package com.growcontrol.gcServer.logger;

import com.growcontrol.gcServer.logger.gcLogger.LEVEL;


public interface gcLoggerHandler {


	public gcLoggerHandler setStrip(boolean enabled);
	public void setLogLevel(LEVEL level);
	public void print(gcLogRecord logRecord);


}
