package com.growcontrol.gcServer.logger;

import com.growcontrol.gcServer.logger.gcLogger.LEVEL;


public interface gcLoggerHandler {


	public void setLogLevel(LEVEL level);
	public void print(gcLogRecord logRecord);
	public void print(String msg);


}
