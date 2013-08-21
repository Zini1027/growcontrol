package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaTrigger implements metaValue {
	private static final long serialVersionUID = 9L;

	protected final Object lock = new Object();


	// static type
	public static final metaType TRIGGER = new metaType("TRIGGER",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaTrigger();
			}
	});


	// instance
	public metaTrigger() {}
	public metaTrigger(Boolean value) {}
	public metaTrigger(metaTrigger meta) {}
	@Override
	public metaValue clone() {
		return new metaTrigger(this);
	}


	// type
	@Override
	public metaType getType() {
		return TRIGGER;
	}


	// get value
	@Override
	public String getString() {
		return null;
	}


	// set value
	public void set(String value) {}


}
