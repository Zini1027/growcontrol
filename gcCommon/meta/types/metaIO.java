package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.pxnMetaType;


public class metaIO extends pxnMetaType {
	private static final long serialVersionUID = 7L;

	protected volatile Boolean value = null;
	protected final Object lock = new Object();


	public metaIO(String name) {
		super(name);
	}
	public metaIO(String name, String value) {
		super(name);
		set(value);
	}
	public metaIO(String name, Boolean value) {
		super(name);
		set(value);
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
		return Boolean.toString(get());
	}


}
