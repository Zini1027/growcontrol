package com.growcontrol.gcCommon.pxnLogger;


public interface pxnLoggerInterface {

	//### pxnLogger ###//

	// get logger
	public pxnLogger get(String name);
	public pxnLogger getAnon(String name);
	public pxnLogger getLogger(String name, boolean anon);

	// parent logger
	public pxnLogger getParent();

	// name
	public String getName();
	public String getNameFormatted();
	// bracers
	public void setBracers(String bracerL, String bracerR);
	// level
	public pxnLevel getLevel();
	public pxnLevel getLevel(String handlerName);
	public void setLevel(pxnLevel level);
	public void setLevel(String handlerName, pxnLevel level);
	public boolean isLoggable(pxnLevel level);
	public boolean isDebug();

	// print to handlers
	public void Publish(pxnLogRecord rec);
	public void Publish(String msg);
	public void Publish(pxnLevel level, String msg);
	public void Publish(pxnLevel level, String msg, Throwable ex);
	public void Major(String msg);



	//### pxnLogPrinter ###//

	// print writers
	public void fatal    (String msg);
	public void severe   (String msg);
	public void warning  (String msg);
	public void info     (String msg);
	public void config   (String msg);
	public void debug    (String msg);
	public void fine     (String msg);
	public void finer    (String msg);
	public void finest   (String msg);
	// exception
	public void exception(Throwable ex);
	public void fatal    (Throwable ex);
	public void severe   (Throwable ex);
	public void warning  (Throwable ex);
	public void info     (Throwable ex);
	public void config   (Throwable ex);
	public void debug    (Throwable ex);
	public void fine     (Throwable ex);
	public void finer    (Throwable ex);
	public void finest   (Throwable ex);
	// message with exception
	public void exception(String msg, Throwable ex);
	public void fatal    (String msg, Throwable ex);
	public void severe   (String msg, Throwable ex);
	public void warning  (String msg, Throwable ex);
	public void info     (String msg, Throwable ex);
	public void config   (String msg, Throwable ex);
	public void debug    (String msg, Throwable ex);
	public void fine     (String msg, Throwable ex);
	public void finer    (String msg, Throwable ex);
	public void finest   (String msg, Throwable ex);

}
