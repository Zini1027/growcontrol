package com.growcontrol.gcServer.logger;

import java.util.logging.FileHandler;

import com.growcontrol.gcServer.logger.gcLogger.LEVEL;

public class gcLoggerFile extends Thread implements gcLoggerHandler {

@SuppressWarnings("unused")
	private boolean strip = false;

	// output buffer
@SuppressWarnings("unused")
	private String buffer = "";
@SuppressWarnings("unused")
	private FileHandler fileHandler = null;


	public gcLoggerFile() {
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
	}


	@Override
	public void print(gcLogRecord logRecord) {
		if(logRecord == null) throw new NullPointerException("logRecord cannot be null");
//		fileHandler
	}
	@Override
	public void print(String msg) {
		if(msg == null) throw new NullPointerException("msg cannot be null");
//		fileHandler
	}


	@Override
	public void setLogLevel(LEVEL level) {
//		this.level = level;
	}


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
