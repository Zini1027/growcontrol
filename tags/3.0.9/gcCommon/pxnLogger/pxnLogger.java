package com.growcontrol.gcCommon.pxnLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jline.console.ConsoleReader;

import com.growcontrol.gcCommon.pxnLogger.handlers.pxnLogHandler;
import com.growcontrol.gcCommon.pxnLogger.handlers.pxnLogHandlerConsole;


public class pxnLogger extends pxnLogPrinter implements pxnLoggerInterface {

	private final HashMap<String, pxnLogger> loggers = new HashMap<String, pxnLogger>();
	private final static List<pxnLogHandler> handlers = new ArrayList<pxnLogHandler>();
	private static volatile boolean inited = false;

	private final String name;
	private volatile String bracerL = "[";
	private volatile String bracerR = "]";
	private final pxnLogger parent;
	private volatile pxnLevel level = pxnLevel.INFO;


	protected pxnLogger(String name, pxnLogger parent) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
		this.name = name;
		this.parent = parent;
		Init();
	}
	protected static synchronized void Init() {
		if(inited) return;
		if(handlers.isEmpty())
			handlers.add(pxnLogHandlerConsole.get());
		inited = true;
	}


	// add handler
	public static void addHandler(pxnLogHandler handler) {
		synchronized(handlers) {
			handlers.add(handler);
		}
	}


	// get logger
	@Override
	public pxnLogger get(String name) {
		return getLogger(name, false);
	}
	@Override
	public pxnLogger getAnon(String name) {
		return getLogger(name, true);
	}
	@Override
	public pxnLogger getLogger(String name, boolean anon) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
		synchronized(loggers) {
			String nameL = name.toLowerCase();
			if(loggers.containsKey(nameL))
				return loggers.get(nameL);
			// new logger
			pxnLogger log = new pxnLogger(name, this);
			log.setLevel(getLevel());
			if(!anon)
				loggers.put(nameL, log);
			return log;
		}
	}


	// parent logger
	@Override
	public pxnLogger getParent() {
		return parent;
	}
//	@Override
//	public List<pxnLogHandler> getHandlers() {
//		return handlers;
//	}


	// name
	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getNameFormatted() {
		return bracerL+getName()+bracerR;
	}


	// bracers [ ] or ( )
	@Override
	public void setBracers(String bracerL, String bracerR) {
		if(bracerL != null)
			this.bracerL = bracerL;
		if(bracerR != null)
			this.bracerR = bracerR;
	}

	// level
	@Override
	public pxnLevel getLevel() {
		return level;
	}
	@Override
	public pxnLevel getLevel(String handlerName) {
//TODO:
		return level;
	}
	@Override
	public void setLevel(pxnLevel level) {
		if(level == null) return;
		if(level.getValue() == this.level.getValue()) return;
//TODO: figure out why this prints so often
		if(pxnLevel.OFF.equals(level))
			pxnLog.get().Publish(level, "Set log level: "+pxnLevel.OFF.toString());
		setLevels(level);
		if(!pxnLevel.OFF.equals(level))
			pxnLog.get().Publish(level, "Set log level: "+level.toString());
	}
	private void setLevels(pxnLevel level) {
		if(level == null) throw new NullPointerException("level cannot be null!");
		// hasn't changed
		if(level.equals(this.level)) return;
		this.level = level;
		// set child levels
		synchronized(loggers) {
			for(pxnLogger log : loggers.values())
				log.setLevels(level);
		}
	}
	@Override
	public void setLevel(String handlerName, pxnLevel level) {
//TODO:
		setLevel(level);
	}
	// is level loggable
	@Override
	public boolean isLoggable(pxnLevel level) {
		return getLevel().isLoggable(level);
	}
	@Override
	public boolean isDebug() {
		return isLoggable(pxnLevel.DEBUG);
	}


	@Override
	public void Publish(pxnLogRecord rec) {
		if(!isLoggable(rec.getLevel())) return;
		if(parent != null) {
			parent.Publish(rec);
			return;
		}
		// pass to handlers
		synchronized(handlers) {
			for(pxnLogHandler h : handlers)
				h.Publish(rec);
		}
	}
	@Override
	public void Publish(String msg) {
		if(msg == null) msg = "<null>";
		synchronized(handlers) {
			for(pxnLogHandler h : handlers)
				h.Publish(msg);
		}
	}
	@Override
	public void Major(String msg) {
		if(msg == null) msg = "<null>";
		Publish(" [[ "+msg+" ]]");
	}
	@Override
	public void Publish(pxnLevel level, String msg) {
		Publish(pxnLogRecord.Create(this, level, msg));
	}
	@Override
	public void Publish(pxnLevel level, String msg, Throwable ex) {
		Publish(pxnLogRecord.Create(this, level, msg, ex));
	}


	// clear screen
	public static void ClearScreen() {
		ConsoleReader reader = pxnConsole.getReader();
		try {
			reader.clearScreen();
			reader.flush();
		} catch (IOException e) {
			pxnLog.get().exception(e);
		}
	}


}
