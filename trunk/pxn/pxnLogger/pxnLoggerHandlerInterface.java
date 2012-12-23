package com.poixson.pxnLogger;

import com.poixson.pxnLogger.pxnLevel.LEVEL;


public interface pxnLoggerHandlerInterface {

	// print
	public void print(pxnLogRecord logRecord);
	public void print(String msg);

	// log level
	public pxnLevel getLevel();
	public void setLevel(LEVEL level);
	public void setForceDebug(boolean forceDebug);

}
