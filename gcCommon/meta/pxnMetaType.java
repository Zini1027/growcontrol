package com.growcontrol.gcCommon.meta;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcCommon.meta.types.metaCommand;
import com.growcontrol.gcCommon.meta.types.metaEC;
import com.growcontrol.gcCommon.meta.types.metaIO;
import com.growcontrol.gcCommon.meta.types.metaThermal;
import com.growcontrol.gcCommon.meta.types.metaVariable;
import com.growcontrol.gcCommon.pxnListener.pxnEvent;


public abstract class pxnMetaType extends pxnEvent implements java.io.Serializable {
	private static final long serialVersionUID = 7L;

	// known types
	private static final transient HashMap<String, pxnMetaType> knownTypes = new HashMap<String, pxnMetaType>();

	// default types
	public static final transient pxnMetaType IO       = new metaIO      ("IO");
	public static final transient pxnMetaType THERMAL  = new metaThermal ("THERMAL");
	public static final transient pxnMetaType VARIABLE = new metaVariable("VARIABLE");
	public static final transient pxnMetaType COMMAND  = new metaCommand ("COMMAND");
	public static final transient pxnMetaType EC       = new metaEC      ("EC");

	private volatile String toName = null;

//	public abstract pxnMetaType clone();
	public abstract void set(String value);
	public abstract String toString();


//	// clone
//	public static pxnMetaType getClone(String name) {
//		pxnMetaType meta = get(name);
//		if(meta == null) return null;
//		return meta.clone();
//	}
//	@Override
//	public Object clone() throws CloneNotSupportedException {
//		throw new CloneNotSupportedException();
//	}


	// send to
	public void To(String toName) {
		if(toName == null || toName.isEmpty()) {
			this.toName = null;
			return;
		}
		this.toName = toName;
	}
	// send to router
	public void Send() {
		metaRouter.get().Send(toName, this);
	}
	public void SendTo(String toName) {
		To(toName);
		Send();
	}


	public pxnMetaType() {}
//	// new meta object
//	public pxnMetaType(pxnMetaType meta) {
//		this(getName(meta));
//	}
	// type singleton
	protected pxnMetaType(String name) {
		registerKnownType(name, this);
	}
	protected static void registerKnownType(String name, pxnMetaType meta) {
		if(name == null) throw new NullPointerException("name cannot be null!");
		if(meta == null) throw new NullPointerException("meta cannot be null!");
		name = name.toUpperCase();
		synchronized(knownTypes) {
			if(knownTypes.containsKey(name)) return;
			knownTypes.put(name, meta);
		}
	}


	// get known type
	public static pxnMetaType get(String name) {
		if(name == null || name.isEmpty()) return null;
		synchronized(knownTypes) {
			if(knownTypes.containsKey(name.toUpperCase()))
				return knownTypes.get(name.toUpperCase());
		}
		// type not found
		return null;
	}
	public static String getName(pxnMetaType meta) {
		synchronized(knownTypes) {
			if(knownTypes.containsValue(meta)) {
				for(Entry<String, pxnMetaType> entry : knownTypes.entrySet()) {
					if(entry.getValue().equals(meta))
						return entry.getKey();
				}
			}
		}
		return null;
	}


//	public static String toString(pxnMeta data) {
//		return data.toString();
//	}


}
