package com.gcCommon.pxnLogger;

import com.gcCommon.pxnLogger.pxnLevel.LEVEL;


public interface pxnLoggerHandler {
//TODO:
//public abstract class pxnLoggerHandler extends PrintWriter {

	// print
	public void print(pxnLogRecord logRecord);
	public void print(String msg);

	// log level
	public pxnLevel getLevel();
	public void setLevel(LEVEL level);
	public void setForceDebug(boolean forceDebug);

}
