package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaIO extends metaValue {
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
			@Override
			public metaValue newValue(String value) {
				return new metaIO(value);
			}
	});
	public static void Init() {
		if(IO == null) System.out.println("Failed to load meta type IO!");
	}


	// instance
	public metaIO() {
		set((Boolean) null);
	}
	public metaIO(Boolean value) {
		set(value);
	}
	public metaIO(String value) {
		set(value);
	}
	public metaIO(metaValue meta) {
		if(meta instanceof metaIO)
			set( ((metaIO) meta).getValue() );
		else
			set(meta.getString());
	}
	@Override
	public metaValue clone() {
		return new metaIO(this);
	}


	// type
	@Override
	public metaType getType() {
		return IO;
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
		Boolean b = getValue();
		if(b == null) return null;
		return (getValue() ? "ON" : "OFF");
	}
	public static String toString(metaIO meta) {
		if(meta == null)
			return null;
		return meta.getString();
	}


	// set value
	public void set(Boolean value) {
		synchronized(lock) {
			this.value = value;
		}
	}
	@Override
	public void set(String value) {
		if(value == null || value.isEmpty()) {
			set((Boolean) null);
			return;
		}
		Boolean b = pxnUtils.toBoolean(value);
		set(b);
	}


}
