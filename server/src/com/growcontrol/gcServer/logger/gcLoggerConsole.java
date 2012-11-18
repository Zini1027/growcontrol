package com.growcontrol.gcServer.logger;

import java.io.IOException;

import jline.ConsoleReader;

import com.growcontrol.gcServer.logger.gcLogger.LEVEL;

public class gcLoggerConsole implements gcLoggerHandler {

	private final jline.ConsoleReader reader;
	private LEVEL level;


	public gcLoggerConsole(jline.ConsoleReader reader, LEVEL level) {
		if(reader == null) throw new NullPointerException();
		if(level  == null) throw new NullPointerException();
		this.reader = reader;
		this.level  = level;
	}


	@Override
	public void print(gcLogRecord logRecord) {
		if(logRecord == null) throw new NullPointerException();
		if(!gcLogger.isLoggable(level, logRecord.level)) return;
		print(logRecord.toString());
	}
	@Override
	public void print(String msg) {
		if(msg == null) throw new NullPointerException();
		// no console handler
		if(reader == null) {
			System.out.print(msg);
			return;
		}
		// print using jansi
		try {
			reader.printString(ConsoleReader.RESET_LINE+"");
			reader.printString(msg);
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
