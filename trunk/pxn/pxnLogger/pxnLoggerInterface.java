package com.poixson.pxnLogger;

import com.poixson.pxnLogger.pxnLevel.LEVEL;


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
	public void print(String msg, LEVEL level);
	public void printRaw(pxnLogRecord logRecord);
	public void printRaw(String msg);

}
