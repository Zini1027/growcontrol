package com.growcontrol.gcServer.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.growcontrol.gcServer.gcServer;

public class gcLogger {

	protected String postfix = null;

	// log levels
	public static enum LEVEL {DEBUG, INFO, WARNING, SEVERE, FATAL};
	protected static final int LEVEL_DEBUG   = 50;
	protected static final int LEVEL_INFO    = 40;
	protected static final int LEVEL_WARNING = 30;
	protected static final int LEVEL_SEVERE  = 10;
	protected static final int LEVEL_FATAL   = 0;

	protected static LEVEL consoleLevel = LEVEL.DEBUG;
	protected static LEVEL fileLevel    = LEVEL.DEBUG;

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
				reader.setDefaultPrompt(gcServer.prompt);
				//reader.setDebug(new PrintWriter(new FileWriter("writer.debug", true)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// log to console
		logHandlers.add(new gcLoggerConsole(reader, consoleLevel));
		// log to file
//		logHandlers.add(new gcLoggerFile().setStrip(true));
		inited = true;
	}


	// read console input
	public static String readLine() {
		if(reader == null) return null;
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
		return line;
	}
	// clear console
	public void clear() {
		if(reader == null) return;
		AnsiConsole.out.println(Ansi.ansi()
			.eraseScreen()
			.cursor(0, 0) );
	}


	// log levels
	public static String levelToString(LEVEL level) {
		if(level != null) {
			if(level.equals(LEVEL.FATAL))	return "FATAL";
			if(level.equals(LEVEL.SEVERE))	return "SEVERE";
			if(level.equals(LEVEL.WARNING))	return "WARNING";
			if(level.equals(LEVEL.INFO))	return "info";
		}
		return "debug";
	}
	public static LEVEL levelFromString(String level) {
		if(level != null) {
			if(level.equalsIgnoreCase("fatal"))		return LEVEL.FATAL;
			if(level.equalsIgnoreCase("severe"))	return LEVEL.SEVERE;
			if(level.equalsIgnoreCase("warning"))	return LEVEL.WARNING;
			if(level.equalsIgnoreCase("info"))		return LEVEL.INFO;
		}
		return LEVEL.DEBUG;
	}
	public static int levelToInt(LEVEL level) {
		if(level != null) {
			if(level.equals(LEVEL.FATAL))	return LEVEL_FATAL;
			if(level.equals(LEVEL.SEVERE))	return LEVEL_SEVERE;
			if(level.equals(LEVEL.WARNING))	return LEVEL_WARNING;
			if(level.equals(LEVEL.INFO))	return LEVEL_INFO;
		}
		return LEVEL_DEBUG;
	}


	// set log level
	public void setLogLevel(LEVEL level) {
		if(level == null) return;
		consoleLevel = level;
		for(gcLoggerHandler handler : logHandlers)
			handler.setLogLevel(level);
		debug("Set log level to: "+levelToString(level));
	}
	public void setLogLevel(String level) {
		setLogLevel(levelFromString(level));
	}


//	public boolean isLoggable(int level) {
////TODO: finish this
//		return true;
//	}
	public boolean isDebug() {
		return true;
//		return (consoleLevel >= LEVEL_DEBUG || fileLevel >= LEVEL_DEBUG);
	}


	// set custom prompt
	public static void setPrompt(String newPrompt) {
		try {
			if(newPrompt == null)
				reader.setDefaultPrompt(gcServer.prompt);
			else
				reader.setDefaultPrompt(newPrompt);
			reader.redrawLine();
			reader.flushConsole();
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
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
	public synchronized void print(String msg, LEVEL level) {
//TODO: this hides extra quarts logs
if(postfix!= null && postfix.equalsIgnoreCase("quartz"))return;
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
