package com.poixson.pxnLogger;

import com.poixson.pxnLogger.pxnLevel.LEVEL;


public interface pxnLoggerHandlerInterface {

//	public void setLogLevel(LEVEL level);
	public void print(pxnLogRecord logRecord);
	public void print(String msg);

	// log level
	public pxnLevel getLevel();
	public void setLevel(LEVEL level);

}
