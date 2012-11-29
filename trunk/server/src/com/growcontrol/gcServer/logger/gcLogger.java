package com.growcontrol.gcServer.logger;

import com.poixson.pxnLogger.pxnLogger;


public class gcLogger extends pxnLogger {


	// get logger
	public static gcLogger getLogger() {
		return getLogger(null);
	}
	public static gcLogger getLogger(String loggerName) {
		return new gcLogger(loggerName);
	}
	// new logger
	public gcLogger() {
		this(null);
	}
	public gcLogger(String loggerName) {
		super(loggerName);
	}


}
