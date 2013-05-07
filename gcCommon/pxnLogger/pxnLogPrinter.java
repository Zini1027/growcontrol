package com.gcCommon.pxnLogger;


public interface pxnLogPrinter {

	// debug
	public void debug(String msg);
	// info
	public void info(String msg);
	// warning
	public void warning(String msg);
	// severe
	public void severe(String msg);
	// fatal error
	public void fatal(String msg);

	// print exception
	public void exception(Throwable e);
	public void exception(String msg, Throwable e);
	public void debug(Throwable e);

}
