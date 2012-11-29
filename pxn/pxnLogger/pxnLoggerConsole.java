package com.poixson.pxnLogger;

import java.io.IOException;


public class pxnLoggerConsole implements pxnLoggerHandlerInterface {

	protected final jline.ConsoleReader reader;
	protected pxnLevel level;


	public pxnLoggerConsole(jline.ConsoleReader reader, pxnLevel level) {
		if(reader == null) throw new NullPointerException("reader cannot be null!");
		if(level  == null) throw new NullPointerException("level cannot be null!");
		this.reader = reader;
		this.level = level;
	}


	// print to console
	@Override
	public void print(pxnLogRecord logRecord) {
		if(logRecord == null) throw new NullPointerException("logRecord cannot be null!");
		if(!pxnLevel.isLoggable(level.getLevel(), logRecord.level)) return;
		print(logRecord.toString());
	}
	@Override
	public void print(String msg) {
		if(msg == null) msg = "null";
		// no console handler
		if(reader == null) {
			System.out.print(msg);
		} else {
			// print using jansi
			try {
				reader.printString(jline.ConsoleReader.RESET_LINE+"");
				reader.printString(msg);
				reader.printNewline();
				reader.flushConsole();
				reader.drawLine();
				reader.redrawLine();
				try {
					reader.drawLine();
				} catch (Throwable e) {
					reader.getCursorBuffer().clearBuffer();
				}
				reader.flushConsole();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.print(msg);
			}
		}
	}


	// logger level
	@Override
	public void setLevel(pxnLevel.LEVEL level) {
		this.level.setLevel(level);
	}
	@Override
	public pxnLevel getLevel() {
throw new NullPointerException("NOT FINISHED!");
//		return null;
	}


}
