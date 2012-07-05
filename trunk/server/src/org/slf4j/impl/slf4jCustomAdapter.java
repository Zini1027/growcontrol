package org.slf4j.impl;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.growcontrol.gcServer.logger.gcLogger;

public final class slf4jCustomAdapter implements Logger {

	private static boolean logDebug = false;

	// level mapping
	public static final int LEVEL_FINEST	= gcLogger.levelToInt(gcLogger.LEVEL.DEBUG);
	public static final int LEVEL_FINE		= gcLogger.levelToInt(gcLogger.LEVEL.DEBUG);
	public static final int LEVEL_INFO		= gcLogger.levelToInt(gcLogger.LEVEL.INFO);
	public static final int LEVEL_WARNING	= gcLogger.levelToInt(gcLogger.LEVEL.WARNING);
	public static final int LEVEL_SEVERE	= gcLogger.levelToInt(gcLogger.LEVEL.SEVERE);

	private gcLogger log;

	slf4jCustomAdapter() {
		this.log = gcLogger.getLogger("quartz");
	}
	slf4jCustomAdapter(gcLogger log) {
		this.log = log;
	}



//	public boolean isLoggable(Level level) {
//		if(level.equals(Level.FINEST))
//			return logger.isLoggable(gcLogger.LEVEL_DEBUG);
//		else if(level.equals(Level.FINER))
//			return logger.isLoggable(gcLogger.LEVEL_DEBUG);
//		else if(level.equals(Level.FINE))
//			return logger.isLoggable(gcLogger.LEVEL_DEBUG);
//		else if(level.equals(Level.INFO))
//			return logger.isLoggable(gcLogger.LEVEL_INFO);
//		else if(level.equals(Level.WARNING))
//			return logger.isLoggable(gcLogger.LEVEL_WARNING);
//		else if(level.equals(Level.SEVERE))
//			return logger.isLoggable(gcLogger.LEVEL_SEVERE);
//		else if(level.equals(Level.CONFIG))
//			return logger.isLoggable(gcLogger.LEVEL_DEBUG);
//		else if(level.equals(Level.ALL))
//			return logger.isLoggable(gcLogger.LEVEL_DEBUG);
//		else if(level.equals(Level.OFF))
//			return logger.isLoggable(gcLogger.LEVEL_FATAL);
//		return true;
//	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


	// trace log
	@Override
	public boolean isTraceEnabled() {
		return log.isDebug();
	}
	@Override
	public boolean isTraceEnabled(Marker marker) {
		debug("isTraceEnabled: "+marker);
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void trace(String msg) {
		if(!logDebug) return;
		log.debug(msg);
	}
	@Override
	public void trace(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		trace(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void trace(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		trace(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void trace(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		trace(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void trace(String msg, Throwable t) {
		trace(msg);
		log.exception(t);
	}
	@Override
	public void trace(Marker marker, String msg) {
		// TODO Auto-generated method stub
		trace("[marker] "+msg);
	}
	@Override
	public void trace(Marker marker, String msg, Object arg) {
		// TODO Auto-generated method stub
		trace("[marker] "+msg);
	}
	@Override
	public void trace(Marker marker, String msg, Object[] argArray) {
		// TODO Auto-generated method stub
		trace("[marker] "+msg);
	}
	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		// TODO Auto-generated method stub
		trace("[marker] "+msg);
	}
	@Override
	public void trace(Marker marker, String msg, Object arg, Object arg3) {
		// TODO Auto-generated method stub
		trace("[marker] "+msg);
	}


	// debug log
	@Override
	public boolean isDebugEnabled() {
		return log.isDebug();
	}
	@Override
	public boolean isDebugEnabled(Marker marker) {
		debug("isDebugEnabled: "+marker);
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void debug(String msg) {
		if(!logDebug) return;
		log.debug(msg);
	}
	@Override
	public void debug(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		debug(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void debug(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		debug(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void debug(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		debug(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void debug(String msg, Throwable t) {
		debug(msg);
		log.exception(t);
	}
	@Override
	public void debug(Marker arg0, String msg) {
		// TODO Auto-generated method stub
		debug("[marker] "+msg);
	}
	@Override
	public void debug(Marker arg0, String msg, Object arg2) {
		// TODO Auto-generated method stub
		debug("[marker] "+msg);
	}
	@Override
	public void debug(Marker arg0, String msg, Object[] arg2) {
		// TODO Auto-generated method stub
		debug("[marker] "+msg);
	}
	@Override
	public void debug(Marker arg0, String msg, Throwable arg2) {
		// TODO Auto-generated method stub
		debug("[marker] "+msg);
	}
	@Override
	public void debug(Marker arg0, String msg, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		debug("[marker] "+msg);
	}


	// info log
	@Override
	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isInfoEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void info(String msg) {
//		debug(msg);
		log.info(msg);
	}
	@Override
	public void info(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		info(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void info(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		info(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void info(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		info(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void info(String msg, Throwable t) {
		info(msg);
		log.exception(t);
	}
	@Override
	public void info(Marker arg0, String msg) {
		// TODO Auto-generated method stub
		info("[marker]"+msg);
	}
	@Override
	public void info(Marker arg0, String msg, Object arg2) {
		// TODO Auto-generated method stub
		info("[marker]"+msg);
	}
	@Override
	public void info(Marker arg0, String msg, Object[] arg2) {
		// TODO Auto-generated method stub
		info("[marker]"+msg);
	}
	@Override
	public void info(Marker arg0, String msg, Throwable arg2) {
		// TODO Auto-generated method stub
		info("[marker]"+msg);
	}
	@Override
	public void info(Marker arg0, String msg, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		info("[marker]"+msg);
	}


	// warning log
	@Override
	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isWarnEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void warn(String msg) {
		log.warning(msg);
	}
	@Override
	public void warn(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		warn(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void warn(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		warn(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void warn(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		warn(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void warn(String msg, Throwable t) {
		warn(msg);
		log.exception(t);
	}
	@Override
	public void warn(Marker arg0, String msg) {
		// TODO Auto-generated method stub
		warn("[marker]"+msg);
	}
	@Override
	public void warn(Marker arg0, String msg, Object arg2) {
		// TODO Auto-generated method stub
		warn("[marker]"+msg);
	}
	@Override
	public void warn(Marker arg0, String msg, Object[] arg2) {
		// TODO Auto-generated method stub
		warn("[marker]"+msg);
	}
	@Override
	public void warn(Marker arg0, String msg, Throwable arg2) {
		// TODO Auto-generated method stub
		warn("[marker]"+msg);
	}
	@Override
	public void warn(Marker arg0, String msg, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		warn("[marker]"+msg);
	}


	// error log
	@Override
	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isErrorEnabled(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void error(String msg) {
		log.severe(msg);
	}
	@Override
	public void error(String format, Object arg) {
		FormattingTuple ft = MessageFormatter.format(format, arg);
		error(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void error(String format, Object arg1, Object arg2) {
		FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		error(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void error(String format, Object[] argArray) {
		FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
		error(ft.getMessage());
		log.exception(ft.getThrowable());
	}
	@Override
	public void error(String msg, Throwable t) {
		error(msg);
		log.exception(t);
	}
	@Override
	public void error(Marker arg0, String msg) {
		// TODO Auto-generated method stub
		error("[marker]"+msg);
	}
	@Override
	public void error(Marker arg0, String msg, Object arg2) {
		// TODO Auto-generated method stub
		error("[marker]"+msg);
	}
	@Override
	public void error(Marker arg0, String msg, Object[] arg2) {
		// TODO Auto-generated method stub
		error("[marker]"+msg);
	}
	@Override
	public void error(Marker arg0, String msg, Throwable arg2) {
		// TODO Auto-generated method stub
		error("[marker]"+msg);
	}
	@Override
	public void error(Marker arg0, String msg, Object arg2, Object arg3) {
		// TODO Auto-generated method stub
		error("[marker]"+msg);
	}


//	public void log(String callerFQCN, Level level, String msg, Throwable t) {
//		logger.
//		if(t != null) logger.exception(t);
//	}
//	public void log(Marker marker, String callerFQCN, int level, String msg, Object[] argArray, Throwable t) {
//		// TODO method stub
//	}


}
