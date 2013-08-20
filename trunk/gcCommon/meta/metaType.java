package com.growcontrol.gcCommon.meta;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcCommon.meta.types.metaCommand;
import com.growcontrol.gcCommon.meta.types.metaEC;
import com.growcontrol.gcCommon.meta.types.metaIO;
import com.growcontrol.gcCommon.meta.types.metaThermal;
import com.growcontrol.gcCommon.meta.types.metaVariable;
import com.growcontrol.gcCommon.pxnListener.pxnEvent;


public abstract class metaType extends pxnEvent implements java.io.Serializable {
	private static final long serialVersionUID = 7L;

	// known types
	private static final transient HashMap<String, metaType> knownTypes = new HashMap<String, metaType>();

	// default types
	public static final transient metaType IO       = new metaIO      ("IO");
	public static final transient metaType THERMAL  = new metaThermal ("THERMAL");
	public static final transient metaType VARIABLE = new metaVariable("VARIABLE");
	public static final transient metaType COMMAND  = new metaCommand ("COMMAND");
	public static final transient metaType EC       = new metaEC      ("EC");

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


	// new dao (value holder)
	public metaType() {}
	// type singleton
	protected metaType(String name) {
		registerKnownType(name, this);
	}
	protected static void registerKnownType(String name, metaType meta) {
		if(name == null) throw new NullPointerException("name cannot be null!");
		if(meta == null) throw new NullPointerException("meta cannot be null!");
		name = name.toUpperCase();
		synchronized(knownTypes) {
			if(knownTypes.containsKey(name)) return;
			knownTypes.put(name, meta);
		}
	}


	// get known type
	public static metaType get(String name) {
		if(name == null || name.isEmpty()) return null;
		synchronized(knownTypes) {
			if(knownTypes.containsKey(name.toUpperCase()))
				return knownTypes.get(name.toUpperCase());
		}
		// type not found
		return null;
	}
	public static String getName(metaType meta) {
		synchronized(knownTypes) {
			if(knownTypes.containsValue(meta)) {
				for(Entry<String, metaType> entry : knownTypes.entrySet()) {
					if(entry.getValue().equals(meta))
						return entry.getKey();
				}
			}
		}
		return null;
	}


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


//	public static String toString(pxnMeta data) {
//		return data.toString();
//	}


}
