package com.growcontrol.gcCommon.pxnLogger;

import com.growcontrol.gcCommon.pxnLogger.pxnLevel.LEVEL;


public interface pxnLoggerInterface {

	// logger name
	public String getLoggerName();

//	// log level
//	public pxnLevel getLevel(String handlerName);
//	public void setLevel(String handlerName, LEVEL level);
//	public void setLevel(String handlerName, String level);

//	// log handlers
//	public pxnLoggerHandlerInterface getLogHandler(String handlerName);
//	public void addLogHandler(String handlerName, pxnLoggerHandlerInterface handler);

	// print to handlers
//	public void print(String msg);
	public void print(LEVEL level, String msg);
	public void printRaw(pxnLogRecord logRecord);
	public void printRaw(String msg);

}
