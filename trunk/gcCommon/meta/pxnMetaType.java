package com.growcontrol.gcCommon.meta;

import java.util.HashMap;

import com.growcontrol.gcCommon.meta.types.metaCommand;
import com.growcontrol.gcCommon.meta.types.metaEC;
import com.growcontrol.gcCommon.meta.types.metaIO;
import com.growcontrol.gcCommon.meta.types.metaThermal;
import com.growcontrol.gcCommon.meta.types.metaVariable;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public abstract class pxnMetaType implements java.io.Serializable {
	private static final long serialVersionUID = 7L;

	// known types
	private static final transient HashMap<String, pxnMetaType> knownTypes = new HashMap<String, pxnMetaType>();

	// default types
	public static final transient pxnMetaType IO       = new metaIO("IO");
	public static final transient pxnMetaType THERMAL  = new metaThermal("THERMAL");
	public static final transient pxnMetaType VARIABLE = new metaVariable("VARIABLE");
	public static final transient pxnMetaType COMMAND  = new metaCommand("COMMAND");
	public static final transient pxnMetaType EC       = new metaEC("EC");


	public abstract void set(String value);
	public abstract String toString();


	public pxnMetaType(String name) {
		registerKnownType(name, this);
	}
	protected static void registerKnownType(String name, pxnMetaType clss) {
		if(name == null) throw new NullPointerException("name cannot be null!");
		name = name.toUpperCase();
		synchronized(knownTypes) {
			if(knownTypes.containsKey(name))
				return;
			pxnMetaType clone;
			try {
				clone = (pxnMetaType) clss.clone();
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
