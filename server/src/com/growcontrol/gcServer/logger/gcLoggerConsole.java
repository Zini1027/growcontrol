package com.growcontrol.gcServer.logger;

import java.io.IOException;

import jline.ConsoleReader;

import com.growcontrol.gcServer.logger.gcLogger.LEVEL;

public class gcLoggerConsole implements gcLoggerHandler {

private boolean strip = false;

	private final jline.ConsoleReader reader;
	private LEVEL level;


	public gcLoggerConsole(jline.ConsoleReader reader, LEVEL level) {
		if(reader == null) throw new NullPointerException();
		if(level  == null) throw new NullPointerException();
		this.reader = reader;
		this.level  = level;
	}


	public gcLoggerHandler setStrip(boolean enabled) {
		strip = enabled;
		return this;
	}


	public void print(gcLogRecord logRecord) {
		if(logRecord == null) throw new NullPointerException();
		if(!gcLogger.isLoggable(level, logRecord.level)) return;
//		if( gcLogger.levelToInt(logRecord.level) > gcLogger.levelToInt(level) ) return;
		if(reader == null) {
			System.out.print(logRecord.toString());
			return;
		}
		try {
			reader.printString(ConsoleReader.RESET_LINE+"");
			reader.printString(logRecord.toString(strip));
			reader.printNewline();
			reader.flushConsole();
//			reader.drawLine();
			reader.redrawLine();
//			try {
//				reader.drawLine();
//			} catch (Throwable e) {
//				reader.getCursorBuffer().clearBuffer();
//			}
			reader.flushConsole();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void setLogLevel(LEVEL level) {
		this.level = level;
	}


}
