package com.growcontrol.gcServer.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcServer.gcServer;

public class gcLogger {
	private static final String prompt = "GC>";

	protected String postfix = null;

	// log levels
	public static enum LEVEL {DEBUG, INFO, WARNING, SEVERE, FATAL};
	protected static LEVEL consoleLevel = LEVEL.INFO;
	protected static LEVEL fileLevel    = LEVEL.INFO;

	// loggers
	protected static List<gcLogger> loggers = new ArrayList<gcLogger>();
	protected static List<gcLoggerHandler> logHandlers = new ArrayList<gcLoggerHandler>();
	private static boolean inited = false;

	// jLine reader
	protected static jline.ConsoleReader reader = null;
	// jAnsi
	protected static PrintWriter out = new PrintWriter(AnsiConsole.out);


	// get logger
	public static synchronized gcLogger getLogger(String name) {
		for(gcLogger logger : loggers)
			if(logger.postfix.equalsIgnoreCase(name))
				return logger;
		return new gcLogger(name);
	}
	protected gcLogger(String name) {
		if(!inited) init();
		postfix = name;
	}


	// init logger
	public static synchronized void init() {
		if(inited) return;
		loggers.clear();
		logHandlers.clear();
		// init jline reader
		if(gcServer.isConsoleEnabled() && reader == null) {
			try {
				reader = new jline.ConsoleReader();
				reader.setBellEnabled(false);
				reader.setUseHistory(true);
				reader.setDefaultPrompt(prompt);
				//reader.setDebug(new PrintWriter(new FileWriter("writer.debug", true)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// log to console
		logHandlers.add(new gcLoggerConsole(reader));
		// log to file
//		logHandlers.add(new gcLoggerFile().setStrip(true));
		inited = true;
	}


	// read console input
	public static String readLine() {
		if(reader == null) return null;
		String line = null;
		try {
			line = reader.readLine(prompt);
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
		return line;
	}
	// clear console
	public void clear() {
		if(reader == null) return;
		try {
			reader.clearScreen();
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
	}


	// log levels
	protected static final int LEVEL_DEBUG		= 50;
	protected static final int LEVEL_INFO		= 40;
	protected static final int LEVEL_WARNING	= 30;
	protected static final int LEVEL_SEVERE		= 10;
	protected static final int LEVEL_FATAL		= 0;
	public static String levelToString(LEVEL level) {
		if(level.equals(LEVEL.FATAL))	return "FATAL";
		if(level.equals(LEVEL.SEVERE))	return "SEVERE";
		if(level.equals(LEVEL.WARNING))	return "WARNING";
		if(level.equals(LEVEL.INFO))	return "info";
										return "debug";
	}
	public static int levelToInt(LEVEL level) {
		if(level.equals(LEVEL.FATAL))	return LEVEL_FATAL;
		if(level.equals(LEVEL.SEVERE))	return LEVEL_SEVERE;
		if(level.equals(LEVEL.WARNING))	return LEVEL_WARNING;
		if(level.equals(LEVEL.INFO))	return LEVEL_INFO;
										return LEVEL_DEBUG;
	}
//	public boolean isLoggable(int level) {
////TODO: finish this
//		return true;
//	}
	public boolean isDebug() {
		return true;
//		return (consoleLevel >= LEVEL_DEBUG || fileLevel >= LEVEL_DEBUG);
	}


	// stack trace
	public void exception(Throwable e) {
		if(e == null) return;
//		this.severe(e.getStackTrace());
		e.printStackTrace();
//		Throwable throwable = logrecord.getThrown();
//		if (throwable != null) {
//			StringWriter stringwriter = new StringWriter();
//			throwable.printStackTrace(new PrintWriter(stringwriter));
//			stringbuilder.append(stringwriter.toString());
//		}
	}


	// print to handlers
	public void print(String msg, LEVEL level) {
		gcLogRecord logRecord = new gcLogRecord(msg, level, postfix);
		for(gcLoggerHandler handler : logHandlers)
			handler.print(logRecord);
	}
	public void print(String line) {
		print(line, LEVEL.INFO);
	}


	// debug
	public void debug(String msg) {
		print(msg, LEVEL.DEBUG);
	}
	// info
	public void info(String msg) {
		print(msg, LEVEL.INFO);
	}
	// warning
	public void warning(String msg) {
		print(msg, LEVEL.WARNING);
	}
	// severe
	public void severe(String msg) {
		print(msg, LEVEL.SEVERE);
	}
	// fatal error
	public void fatal(String msg) {
		print(msg, LEVEL.FATAL);
		System.exit(1);
	}


}
