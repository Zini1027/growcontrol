package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaIO implements metaValue {
	private static final long serialVersionUID = 9L;

	// raw value
	protected volatile Boolean value = null;
	protected final Object lock = new Object();


	// static type
	public static final metaType IO = new metaType("IO",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaIO();
			}
	});


	// instance
	public metaIO() {
		set((Boolean) null);
	}
	public metaIO(Boolean value) {
		set(value);
	}
	public metaIO(metaIO meta) {
		this(meta.getValue());
	}
	@Override
	public metaValue clone() {
		return new metaIO(this);
	}


	// type
	@Override
	public metaType getType() {
		return null;
	}


	// get value
	public Boolean getValue() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.booleanValue();
		}
	}
	@Override
	public String getString() {
		return (getValue() ? "on" : "off");
	}


	// set value
	public void set(Boolean value) {
		synchronized(lock) {
			this.value = value;
		}
	}
	public void set(String value) {
		if(value == null || value.isEmpty()) {
			set((Boolean) null);
			return;
		}
		Boolean b = Boolean.valueOf(value);
		set(b);
	}


}
