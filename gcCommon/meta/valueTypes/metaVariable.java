package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;


public class metaVariable extends metaType {
	private static final long serialVersionUID = 7L;

	protected volatile Integer value = null;
	protected volatile Integer override = null;
	protected volatile int min = 0;
	protected volatile int max = 1;
	protected final Object lock = new Object();


	// new meta object
	public static metaVariable newValue(Integer value) {
		metaVariable meta = new metaVariable();
		meta.set(value);
		return meta;
	}
	// new dao (value holder)
	public metaVariable() {}
	// type singleton
	public metaVariable(String name) {
		super(name);
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
			this.value = pxnUtils.MinMax(value.intValue(), min, max);
		}
	}
	@Override
	public void set(String value) {
		Integer i = null;
		try {
			i = pxnUtils.toNumber(value);
		} catch (Exception ignore) {
			return;
		}
		if(i == null) return;
		set(i);
	}
	// set override
	public void setOverride(Integer value) {
		synchronized(lock) {
			// set null
			if(value == null) {
				this.override = null;
				return;
			}
			// set value
			this.override = pxnUtils.MinMax(value.intValue(), min, max);
		}
	}
	public void setOverride(String value) {
		Integer i = null;
		try {
			i = pxnUtils.toNumber(value);
		} catch (Exception ignore) {
			return;
		}
		if(i == null) return;
		set(i);
	}


	// get value
	public Integer get() {
		synchronized(lock) {
			if(override != null)
				return override.intValue();
			if(value != null)
				return value.intValue();
		}
		return null;
	}
	@Override
	public String toString() {
		Integer v = get();
		if(v == null)
			return null;
		return Integer.toString(v);
	}


//	public void setMinValue(int value) {
//		this.min = value;
//	}
//	public void setMaxValue(int value) {
//		this.max = value;
//	}
//	public void setDisabledValue(int value) {
//		this.disabled = value;
//	}


}
