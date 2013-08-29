package com.growcontrol.gcCommon.pxnLogger;

import com.growcontrol.gcCommon.pxnLogger.handlers.pxnLogHandler;


public final class pxnLog {
	private pxnLog() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static final String MainLoggerName = "main";
	private static pxnLogger mainLogger = null;
	private static final Object lock = new Object();


	// get main logger
	public static pxnLogger get() {
		if(mainLogger == null) {
			synchronized(lock) {
				if(mainLogger == null)
					mainLogger = new pxnLogger(MainLoggerName, null);
			}
		}
		return mainLogger;
	}
	// get logger
	public static pxnLogger get(String name) {
		// main logger
		pxnLogger log = get();
		if(name == null || name.isEmpty() || name.toLowerCase().equals(MainLoggerName))
			return log;
		// child logger
		return log.get(name);
	}
	// anonymous logger
	public static pxnLogger getAnon(String name) {
		// main logger
		pxnLogger log = get();
		if(name == null || name.isEmpty() || name.toLowerCase().equals(MainLoggerName))
			return log;
		// child logger
		return log.getAnon(name);
	}


//	// log handlers
//	public static pxnLoggerHandler getLogHandler(String handlerName) {
//		synchronized(logHandlers) {
//			if(logHandlers.containsKey(handlerName))
//				return logHandlers.get(handlerName);
//		}
//		return null;
//	}
	public static void addHandler(pxnLogHandler handler) {
		pxnLogger.addHandler(handler);
	}


}
