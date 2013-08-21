package com.growcontrol.gcCommon.meta;

import java.util.HashMap;

import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public class metaType implements java.io.Serializable {
	private static final long serialVersionUID = 9L;
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	protected final String name;
	protected final valueFactory factory;


	// known types
	private static final HashMap<String, metaType> known = new HashMap<String, metaType>();
	public static void addType(metaType type) {
		if(type == null) throw new NullPointerException("type cannot be null!");
		String name = type.getName();
		synchronized(known) {
			// already exists
			if(known.containsKey(name)) return;
			known.put(name, type);
		}
		pxnLog.get().finer("Added meta type: "+name);
	}


	// new meta type
	public metaType(String name, valueFactory factory) {
		if(name    == null) throw new NullPointerException("name cannot be null!");
		if(factory == null) throw new NullPointerException("factory cannot be null!");
		this.name = name;
		this.factory = factory;
		addType(this);
	}


	public String getName() {
		return name;
	}


}
