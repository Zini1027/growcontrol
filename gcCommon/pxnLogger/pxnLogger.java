package com.growcontrol.gcCommon.pxnLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.growcontrol.gcCommon.pxnLogger.handlers.pxnLogHandler;
import com.growcontrol.gcCommon.pxnLogger.handlers.pxnLogHandlerConsole;


public class pxnLogger extends pxnLogPrinter implements pxnLoggerInterface {

	private final HashMap<String, pxnLogger> loggers = new HashMap<String, pxnLogger>();
	private final static List<pxnLogHandler> handlers = new ArrayList<pxnLogHandler>();
	private static volatile boolean inited = false;

	private final String name;
	private final pxnLogger parent;
	private pxnLevel level = pxnLevel.INFO;
	private static boolean debug = false;


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
		return "["+getName()+"]";
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
		if(level == null) throw new NullPointerException("level cannot be null!");
		this.level = level;
		// set child levels
		synchronized(loggers) {
			for(pxnLogger log : loggers.values())
				log.setLevel(level);
		}
		pxnLog.get().Publish(level, "Set log level: "+level.toString());
	}
	@Override
	public void setLevel(String handlerName, pxnLevel level) {
//TODO:
		setLevel(level);
//		if(handlerName.equalsIgnoreCase("CONSOLE"))
		// update debug mode
		debug = getLevel().isLoggable(pxnLevel.DEBUG);
	}
	// is level loggable
	@Override
	public boolean isLoggable(pxnLevel level) {
		return getLevel().isLoggable(level);
	}
	@Override
	public boolean isDebug() {
		return debug;
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
//TODO:
Publish(msg);
//if(msg == null) msg = "[null]";
//printRaw("[[ "+msg+" ]]");
	}
	@Override
	public void Publish(pxnLevel level, String msg) {
		Publish(pxnLogRecord.Create(this, level, msg));
	}
	@Override
	public void Publish(pxnLevel level, String msg, Throwable ex) {
		Publish(pxnLogRecord.Create(this, level, msg, ex));
	}


}
