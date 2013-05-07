package com.gcCommon.pxnLogger;


public class pxnLevel {

	// log levels
	public static enum LEVEL {
		DEBUG,
		INFO,
		WARNING,
		SEVERE,
		FATAL
	};
	protected static final int LEVEL_DEBUG   = 50;
	protected static final int LEVEL_INFO    = 40;
	protected static final int LEVEL_WARNING = 30;
	protected static final int LEVEL_SEVERE  = 10;
	protected static final int LEVEL_FATAL   = 0;

	protected LEVEL level = LEVEL.INFO;
	protected boolean forceDebug = false;


	public pxnLevel() {
		this(null);
	}
	public pxnLevel(LEVEL level) {
		if(level != null)
			this.level = level;
	}


	// set log level
	public void setLevel(String level) {
		setLevel(levelFromString(level));
	}
	public void setLevel(LEVEL level) {
		if(level == null) throw new NullPointerException("level cannot be null!");
		this.level = level;
//		System.out.println("Set log level to: "+levelToString(level));
		pxnLogger.getLogger().debug("Set log level to: "+levelToString(level));
	}
	// get log level
	public LEVEL getLevel() {
		if(forceDebug)
			return LEVEL.DEBUG;
		return level;
	}
	public String getLevelString() {
		return levelToString(getLevel());
	}
	// force debug mode
	public void setForceDebug(boolean forceDebug) {
		this.forceDebug = forceDebug;
	}


	// test level
	public static boolean isLoggable(LEVEL setLevel, LEVEL testLevel) {
		return isLoggable(setLevel, levelToInt(testLevel));
	}
	public static boolean isLoggable(LEVEL setLevel, int testLevel) {
		if(setLevel == null) throw new NullPointerException();
		return levelToInt(setLevel) >= testLevel;
	}
	public boolean isDebug() {
		return level.equals(LEVEL.DEBUG);
	}


	// log levels
	public static String levelToString(LEVEL level) {
		if(level == null) throw new NullPointerException("level cannot be null!");
		if(level.equals(LEVEL.FATAL))   return "FATAL";
		if(level.equals(LEVEL.SEVERE))  return "SEVERE";
		if(level.equals(LEVEL.WARNING)) return "WARNING";
		if(level.equals(LEVEL.INFO))    return "info";
		return "debug";
	}
	public static LEVEL levelFromString(String level) {
		if(level == null) throw new NullPointerException("level cannot be null!");
		if(level.equalsIgnoreCase("fatal"))   return LEVEL.FATAL;
		if(level.equalsIgnoreCase("severe"))  return LEVEL.SEVERE;
		if(level.equalsIgnoreCase("warning")) return LEVEL.WARNING;
		if(level.equalsIgnoreCase("info"))    return LEVEL.INFO;
		return LEVEL.DEBUG;
	}
	public static int levelToInt(LEVEL level) {
		if(level == null) throw new NullPointerException("level cannot be null!");
		if(level.equals(LEVEL.FATAL))   return LEVEL_FATAL;
		if(level.equals(LEVEL.SEVERE))  return LEVEL_SEVERE;
		if(level.equals(LEVEL.WARNING)) return LEVEL_WARNING;
		if(level.equals(LEVEL.INFO))    return LEVEL_INFO;
		return LEVEL_DEBUG;
	}


}
