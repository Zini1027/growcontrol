package com.growcontrol.gcCommon.meta;

import java.util.HashMap;

import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public abstract class pxnMeta implements java.io.Serializable {
	private static final long serialVersionUID = 7L;

	// known types
	private static final transient HashMap<String, pxnMeta> knownTypes = new HashMap<String, pxnMeta>();

	// default types
	public static final transient pxnMeta IO       = new metaIO("IO");
	public static final transient pxnMeta THERMAL  = new metaThermal("THERMAL");
	public static final transient pxnMeta VARIABLE = new metaVariable("VARIABLE");
	public static final transient pxnMeta COMMAND  = new metaCommand("COMMAND");
	public static final transient pxnMeta EC       = new metaEC("EC");


	public abstract void set(String value);
	public abstract String toString();


	public pxnMeta(String name) {
		registerKnownType(name, this);
	}
	protected static void registerKnownType(String name, pxnMeta clss) {
		if(name == null) throw new NullPointerException("name cannot be null!");
		name = name.toUpperCase();
		synchronized(knownTypes) {
			if(knownTypes.containsKey(name))
				return;
			pxnMeta clone;
			try {
				clone = (pxnMeta) clss.clone();
			} catch (CloneNotSupportedException e) {
				pxnLog.get().exception(e);
				return;
			}
			knownTypes.put(name, clone);
		}
	}


//	public static String toString(pxnMeta data) {
//		return data.toString();
//	}


}
