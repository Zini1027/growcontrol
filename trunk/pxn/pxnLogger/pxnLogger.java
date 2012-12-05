package com.poixson.pxnLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import jline.History;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import com.poixson.pxnLogger.pxnLevel.LEVEL;


public class pxnLogger implements pxnLoggerInterface {

	protected static Boolean inited = false;

	protected String loggerName;

	// loggers
	protected static HashMap<String, pxnLogger> loggers = new HashMap<String, pxnLogger>();
	protected static HashMap<String, pxnLoggerHandlerInterface> logHandlers = new HashMap<String, pxnLoggerHandlerInterface>();

	// console interface
	protected static boolean consoleEnabled = true;
	public static final String defaultPrompt = ">";
	// jLine reader
	protected static jline.ConsoleReader reader = null;
	// jAnsi
	protected static PrintWriter out = new PrintWriter(AnsiConsole.out);


	// get logger
	public static pxnLogger getLogger() {
		return getLogger(null);
	}
	public static pxnLogger getLogger(String loggerName) {
		synchronized(loggers) {
			if(loggers.containsKey(loggerName))
				return loggers.get(loggerName);
			pxnLogger log = new pxnLogger(loggerName);
			loggers.put(loggerName, log);
			return log;
		}
	}
	public static pxnLogger log() {
		return getLogger(null);
	}
	// new logger
	protected pxnLogger() {
		this(null);
	}
	protected pxnLogger(String loggerName) {
		this.loggerName = loggerName;
		if(!inited) init();
	}


	// init logger
	protected void init() {
		synchronized(inited) {
			if(inited) return;
			loggers.clear();
			logHandlers.clear();

			inited = true;
		}
		// jline reader
		if(consoleEnabled && reader == null) {
			try {
				reader = new jline.ConsoleReader();
				reader.setBellEnabled(false);
				reader.setUseHistory(true);
				reader.setDefaultPrompt(defaultPrompt);
				History history = reader.getHistory();
				history.setHistoryFile(new File("history.txt"));
				history.setMaxSize(100);
				//reader.setDebug(new PrintWriter(new FileWriter("writer.debug", true)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//moved to gcServer
//		// log to console
//		logHandlers.add(new gcLoggerConsole(reader, logLevel));
//		// log to file
//		logHandlers.add(new gcLoggerFile().setStrip(true));
	}
	protected boolean isInited() {
		return inited;
	}


	// get logger name
	@Override
	public String getLoggerName() {
		return loggerName;
	}


	// console interface
	public static boolean isConsoleEnabled() {
		return consoleEnabled;
	}
	public static void setConsoleEnabled(boolean enabled) {
		consoleEnabled = enabled;
	}
	// prompt
	public static String getPrompt() {
		return reader.getDefaultPrompt();
	}
	public static void setPrompt(String promptStr) throws IOException {
		if(promptStr == null)
			reader.setDefaultPrompt(defaultPrompt);
		else
			reader.setDefaultPrompt(promptStr);
		reader.redrawLine();
		reader.flushConsole();
	}
	public static jline.ConsoleReader getReader() {
		return reader;
	}


	// read console input
	public static String readLine() throws IOException {
		if(reader == null) throw new NullPointerException("reader can't be null!");
		return reader.readLine();
	}
	// clear console
	public static void clear() {
		if(reader == null) throw new NullPointerException("reader can't be null!");
		AnsiConsole.out.println(Ansi.ansi()
			.eraseScreen()
			.cursor(0, 0) );
	}


	// log levels
	public static pxnLevel getLevel(String handlerName) {
		pxnLoggerHandlerInterface handler = getLogHandler(handlerName);
		if(handler == null) return null;
		return handler.getLevel();
	}
	public static void setLevel(String handlerName, String level) {
		setLevel(handlerName, pxnLevel.levelFromString(level));
	}
	public static void setLevel(String handlerName, LEVEL level) {
		pxnLoggerHandlerInterface handler = getLogHandler(handlerName);
		if(handler == null) throw new NullPointerException(handlerName+" (can't set log level, handler not found!)");
		handler.setLevel(level);
	}


	// log handlers
	public static pxnLoggerHandlerInterface getLogHandler(String handlerName) {
		synchronized(logHandlers) {
			if(logHandlers.containsKey(handlerName))
				return logHandlers.get(handlerName);
		}
		return null;
	}
	public static void addLogHandler(String handlerName, pxnLoggerHandlerInterface handler) {
		synchronized(logHandlers) {
			logHandlers.put(handlerName, handler);
		}
	}


	// new log record
	protected static pxnLogRecord newRecord(String msg, LEVEL level, String loggerName) {
		return new pxnLogRecord(msg, level, loggerName);
	}


	// print to handlers
//	@Override
//	public void print(String msg) {
//		print(msg, LEVEL.INFO);
//	}
	@Override
	public synchronized void print(String msg, LEVEL level) {
		if(msg   == null) throw new NullPointerException("msg cannot be null");
		if(level == null) throw new NullPointerException("level cannot be null");
		printRaw(newRecord(msg, level, loggerName) );
	}
	@Override
	public synchronized void printRaw(pxnLogRecord logRecord) {
		if(logRecord == null) throw new NullPointerException("logRecord cannot be null");
		for(pxnLoggerHandlerInterface handler : logHandlers.values())
			handler.print(logRecord);
	}
	@Override
	public synchronized void printRaw(String msg) {
		if(msg == null) throw new NullPointerException("msg cannot be null");
		for(pxnLoggerHandlerInterface handler : logHandlers.values())
			handler.print(msg);
	}


	// debug
	@Override
	public void debug(String msg) {
		print(msg, LEVEL.DEBUG);
	}
	// info
	@Override
	public void info(String msg) {
		print(msg, LEVEL.INFO);
	}
	// warning
	@Override
	public void warning(String msg) {
		print(msg, LEVEL.WARNING);
	}
	// severe
	@Override
	public void severe(String msg) {
		print(msg, LEVEL.SEVERE);
	}
	// fatal error
	@Override
	public void fatal(String msg) {
		print(msg, LEVEL.FATAL);
		System.exit(1);
	}


	// print exception - stack trace
	@Override
	public void exception(Throwable e) {
		exception(null, e);
	}
	@Override
	public void exception(String msg, Throwable e) {
//TODO: use msg argument
		if(e == null) throw new NullPointerException("e can't be null!");
//		this.severe(e.getStackTrace().toString());
		e.printStackTrace();
//		Throwable throwable = logrecord.getThrown();
//		if (throwable != null) {
//			StringWriter stringwriter = new StringWriter();
//			throwable.printStackTrace(new PrintWriter(stringwriter));
//			stringbuilder.append(stringwriter.toString());
//		}
	}


}