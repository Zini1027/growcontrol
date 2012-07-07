package com.growcontrol.gcServer.logger;

public interface gcLoggerHandler {


	public gcLoggerHandler setStrip(boolean enabled);
	public void print(gcLogRecord logRecord);


}
