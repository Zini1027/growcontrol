package com.growcontrol.gcCommon.pxnLogger;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnUtils;


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

	private static volatile int min = 0;
	private static volatile int max = 0;


	protected pxnLevel(String name, int value) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
		this.name = name.toUpperCase();
		this.value = value;
		// min/max values
		if(value != Integer.MIN_VALUE && value != Integer.MAX_VALUE) {
			if(value < min) min = value;
			if(value > max) max = value;
		}
		// known levels
		synchronized(knownLevels) {
			knownLevels.add(this);
		}
	}


	// parse string to level
	public static pxnLevel Parse(String name) {
		if(name == null || name.isEmpty()) return null;
		if(pxnUtils.isNumeric(name)) {
			Integer i = pxnUtils.toNumber(name);
			if(i != null)
				return FindLevel(i);
		}
		name = name.toUpperCase();
		if(name.equals("ON"))
			return pxnLevel.ALL;
		synchronized(knownLevels) {
			for(pxnLevel level : knownLevels)
				if(name.equals(level.getName()))
					return level;
		}
		return null;
	}
	// find closest level by value, without going under
	public static pxnLevel FindLevel(int value) {
		if(value == pxnLevel.OFF.getValue()) return pxnLevel.OFF;
		if(value == pxnLevel.ALL.getValue()) return pxnLevel.ALL;
		pxnLevel level = null;
		int offBy = 0;
		for(pxnLevel l : knownLevels) {
			if(l.equals(pxnLevel.OFF) || l.equals(pxnLevel.ALL)) continue;
			int lvl = l.getValue();
			// too high
			if(lvl > value) continue;
			int ob = value - lvl;
			if(level == null || ob < offBy) {
				level = l;
				offBy = ob;
				continue;
			}
		}
		if(level == null)
			return pxnLevel.ALL;
		return level;
	}


	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return getName();
	}
	public int getValue() {
		return value;
	}


	// level equals
	public boolean equals(pxnLevel level) {
		if(level == null) return false;
		return (level.value == this.value);
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
