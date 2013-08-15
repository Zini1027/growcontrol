package com.growcontrol.gcCommon.pxnLogger.handlers;

import com.growcontrol.gcCommon.pxnLogger.pxnLogRecord;


public class pxnLogHandlerFile implements pxnLogHandler {
	private static final String handlerName = "FILE";

	// handler instance
	private static volatile pxnLogHandlerFile handler = null;
	private static final Object lock = new Object();


	public static pxnLogHandlerFile get() {
		if(handler == null) {
			synchronized(lock) {
				if(handler == null)
					handler = new pxnLogHandlerFile();
			}
		}
		return handler;
	}
	private pxnLogHandlerFile() {}


	// close
	@Override
	public void Close() {
//TODO:
	}
	@Override
	protected void finalize() {
		Close();
	}


	// handler name
	@Override
	public String getName() {
		return handlerName;
	}


	// print to file
	@Override
	public void Publish(pxnLogRecord rec) {
	}
	@Override
	public void Publish(String msg) {
	}


	@Override
	public void Flush() {
	}


//	public pxnLoggerFile() {
//		try {
//			String pattern = "%h/gcServer%g.log";
//			int limit = 1024 * 1024 * 10; // 10mb
//			int count = 5; // limit to 5 files
//			boolean append = true;
//			fileHandler = new FileHandler(pattern, limit, count, append);
//			fileHandler.setFormatter(consolelogformatter);
//		} catch(Exception e) {
//			global.log(Level.WARNING, "Failed to log to server.log", exception);
//		}
//	}


//	private synchronized void rotate() {
//		Level oldLevel = getLevel();
//		setLevel(Level.OFF);
//		super.close();
//		for (int i = this.count - 2; i >= 0; --i) {
//			File f1 = this.files[i];
//			File f2 = this.files[(i + 1)];
//			if (f1.exists()) {
//				if (f2.exists()) {
//					f2.delete();
//				}
//				f1.renameTo(f2);
//			}
//		}
//		try {
//			open(this.files[0], false);
//		} catch (IOException ix) {
//			reportError(null, ix, 4);
//		}
//		setLevel(oldLevel);
//	}


}
