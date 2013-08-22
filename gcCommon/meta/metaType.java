package com.growcontrol.gcCommon.meta;

import java.util.HashMap;
import java.util.Map.Entry;


public class metaType implements java.io.Serializable {
	private static final long serialVersionUID = 9L;
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	protected final String typeStr;
	protected final valueFactory factory;


	// known types
	private static final HashMap<String, metaType> known = new HashMap<String, metaType>();
	public static void addType(metaType type) {
		if(type == null) {
			(new NullPointerException("type cannot be null!")).printStackTrace();
			return;
		}
		String typeStr = type.getString();
		if(typeStr == null || typeStr.isEmpty()) {
			(new NullPointerException("typeStr cannot be null!")).printStackTrace();
			return;
		}
		typeStr = typeStr.toUpperCase();
		synchronized(known) {
			// already exists
			if(!known.containsKey(typeStr))
				known.put(typeStr, type);
		}
		// can't use logger here
		//pxnLog.get().finer("Added meta type: "+name);
	}
	// string to meta type
	public static metaType Find(String typeStr) {
		if(typeStr == null || typeStr.isEmpty())
			return null;
		typeStr = typeStr.toUpperCase();
		synchronized(known) {
			if(known.containsKey(typeStr))
				return known.get(typeStr);
			for(Entry<String, metaType> entry : known.entrySet()) {
				if(entry.getKey().equalsIgnoreCase(typeStr))
					return entry.getValue();
			}
		}
		return null;
	}


	// new meta type
	public metaType(String name, valueFactory factory) {
		if(name == null)
			(new NullPointerException("name cannot be null!")).printStackTrace();
		if(factory == null)
			(new NullPointerException("factory cannot be null!")).printStackTrace();
		if(name == null || factory == null) {
			this.typeStr = null;
			this.factory = null;
			return;
		}
		this.typeStr = name;
		this.factory = factory;
		addType(this);
	}


	public String getString() {
		return typeStr;
	}


	public static String toString(metaType type) {
		if(type == null)
			return null;
		return type.getString();
	}


}
