package com.growcontrol.gcServer.logger;

import java.io.IOException;

import jline.ConsoleReader;

public class gcLoggerConsole implements gcLoggerHandler {

private boolean strip = false;

	private final jline.ConsoleReader reader;

	public gcLoggerConsole(jline.ConsoleReader reader) {
		this.reader = reader;
	}


	public gcLoggerHandler setStrip(boolean enabled) {
		strip = enabled;
		return this;
	}


	public void print(gcLogRecord logRecord) {
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


}
