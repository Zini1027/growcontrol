package com.growcontrol.gcCommon.pxnLogger;

import java.util.ArrayList;
import java.util.List;


public class pxnLevel implements java.io.Serializable {
	private static final long serialVersionUID = 6L;

	private static final transient List<pxnLevel> knownLevels = new ArrayList<pxnLevel>();

	public static final transient pxnLevel OFF     = new pxnLevel("OFF",     Integer.MAX_VALUE);
	public static final transient pxnLevel FATAL   = new pxnLevel("FATAL",   2000);
	public static final transient pxnLevel SEVERE  = new pxnLevel("SEVERE",  1000);
	public static final transient pxnLevel WARNING = new pxnLevel("WARNING", 900);
	public static final transient pxnLevel INFO    = new pxnLevel("INFO",    800);
	public static final transient pxnLevel CONFIG  = new pxnLevel("CONFIG",  700);
	public static final transient pxnLevel DEBUG   = new pxnLevel("DEBUG",   600);
	public static final transient pxnLevel FINE    = new pxnLevel("FINE",    500);
	public static final transient pxnLevel FINER   = new pxnLevel("FINER",   400);
	public static final transient pxnLevel FINEST  = new pxnLevel("FINEST",  300);
	public static final transient pxnLevel ALL     = new pxnLevel("ALL",     Integer.MIN_VALUE);

	private final String name;
	private final int value;


	protected pxnLevel(String name, int value) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
		this.name = name.toUpperCase();
		this.value = value;
		synchronized(knownLevels) {
			knownLevels.add(this);
		}
	}


	public static pxnLevel findLevel(int value) {
//TODO:
		return null;
	}
	public static pxnLevel parse(String name) {
		if(name == null || name.isEmpty()) return null;
		name = name.toUpperCase();
		synchronized(knownLevels) {
			for(pxnLevel level : knownLevels)
				if(name.equals(level.getName()))
					return level;
		}
		return null;
	}


	public String getName() {
		return name;
	}
	public String toString() {
		return getName();
	}
	public int getValue() {
		return value;
	}


	// is level loggable
	public boolean isLoggable(pxnLevel level) {
		if(level == null) return false;
		int lvl = level.getValue();
		// disabled
		if(value == pxnLevel.OFF.getValue()) return false;
		// forced
		if(value == pxnLevel.ALL.getValue()) return true;
		// is always
		if(lvl == pxnLevel.ALL.getValue()) return true;
		// compare by value
		return value <= lvl;
	}


}
