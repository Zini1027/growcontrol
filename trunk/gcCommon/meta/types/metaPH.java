package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.pxnMetaType;


public class metaPH extends pxnMetaType {
	private static final long serialVersionUID = 7L;

	protected volatile Integer value = null;
	protected final Object lock = new Object();


	public metaPH(String name) {
		super(name);
	}
	public metaPH(String name, String value) {
		super(name);
		set(value);
	}
	public metaPH(String name, Integer value) {
		super(name);
		set(value);
	}


	// set value
	public void set(Integer value) {
		synchronized(lock) {
			// set null
			if(value == null) {
				this.value = null;
				return;
			}
			// set value
			this.value = pxnUtils.MinMax(value.intValue(), 0, 14);
		}
	}
	@Override
	public void set(String value) {
		// set null
		if(value == null) {
			set((Integer) null);
			return;
		}
		// set value
		try {
			set(Integer.valueOf(
				value.replace("p", "").replace("h", "").trim()
			));
		} catch (Exception ignore) {}
	}


	// get value
	public Integer get() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.intValue();
		}
	}
	@Override
	public String toString() {
		Integer value = get();
		if(value == null)
			return null;
		return Integer.toString(get());
	}


}
