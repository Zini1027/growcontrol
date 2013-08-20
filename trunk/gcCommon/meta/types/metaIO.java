package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;


public class metaIO extends metaType {
	private static final long serialVersionUID = 7L;

	protected volatile Boolean value = null;
	protected final Object lock = new Object();


	// new meta object
	public static metaIO newValue(Boolean value) {
		metaIO meta = new metaIO();
		meta.set(value);
		return meta;
	}
	// new dao (value holder)
	public metaIO() {}
	// type singleton
	public metaIO(String name) {
		super(name);
	}


	// set value
	public void set(Boolean value) {
		synchronized(lock) {
			this.value = value;
		}
	}
	@Override
	public void set(String value) {
		// set null
		if(value == null) {
			set((Boolean) null);
			return;
		}
		// set value
		Boolean b = pxnUtils.toBoolean(value);
		if(b == null) return;
		set(b);
	}


	// get value
	public Boolean get() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.booleanValue();
		}
	}
	@Override
	public String toString() {
		Boolean b = get();
		if(b == null)
			return null;
		return b.toString();
	}


}
