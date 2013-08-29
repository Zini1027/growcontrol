package com.growcontrol.gctimer.timers;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gctimer.config.TimerDAO;
import com.growcontrol.gctimer.timers.Clock.ClockFactory;
import com.growcontrol.gctimer.timers.Sequencer.SequencerFactory;
import com.growcontrol.gctimer.timers.Ticker.TickerFactory;


public final class TimerType implements java.io.Serializable {
	private static final long serialVersionUID = 6L;

	private static final transient List<TimerType> knownTypes = new ArrayList<TimerType>();

	public static final transient TimerType CLOCK     = new TimerType("CLOCK",     new ClockFactory()    );
	public static final transient TimerType TICKER    = new TimerType("TICKER",    new TickerFactory()   );
	public static final transient TimerType SEQUENCER = new TimerType("SEQUENCER", new SequencerFactory());

	private final String name;
	private final TimerFactory factory;


	protected TimerType(String name, TimerFactory factory) {
		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
		if(factory == null) throw new NullPointerException("factory cannot be null!");
		this.name = name.toUpperCase();
		this.factory = factory;
		// known types
		synchronized(knownTypes) {
			knownTypes.add(this);
		}
	}


	public static TimerWorker newTimer(TimerDAO dao) {
		if(dao == null) return null;
		if(dao.type == null) return null;
		if(dao.type.factory == null) return null;
		return dao.type.factory.newTimer(dao);
	}
//	public static TimerWorker newTimer(String typeStr, String name) {
//		if(typeStr == null || typeStr.isEmpty()) return null;
//		if(name == null || name.isEmpty()) return null;
//		return newTimer(parse(typeStr), name);
//	}
//	public static TimerWorker newTimer(TimerType type, String name) {
//		if(type == null) return null;
//		if(name == null || name.isEmpty()) throw new NullPointerException("name cannot be null!");
//		return type.factory.getNewTimer(name);
//	}


	// parse string to timer type
	public static TimerType Parse(String typeName) {
		if(typeName == null || typeName.isEmpty()) return null;
		typeName = typeName.toUpperCase();
		synchronized(knownTypes) {
			for(TimerType type : knownTypes) {
				if(typeName.equals(type.getName()))
					return type;
			}
		}
		return null;
	}


	// timer type name
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return getName();
	}


	// timer type equals
	public boolean equals(TimerType type) {
		if(type == null) return false;
		return (this.name.equalsIgnoreCase(type.getName()));
	}
	public boolean nameEquals(String name) {
		if(name == null || name.isEmpty()) return false;
		return name.toUpperCase().equals(this.name);
	}


}
