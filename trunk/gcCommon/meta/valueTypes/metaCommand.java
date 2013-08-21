package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaCommand implements metaValue {
	private static final long serialVersionUID = 9L;

	// raw value
	protected volatile String value = null;
	protected final Object lock = new Object();


	// static type
	public static final metaType COMMAND = new metaType("COMMAND",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaCommand();
			}
	});


	// instance
	public metaCommand() {
		set((String) null);
	}
	public metaCommand(String value) {
		set(value);
	}
	public metaCommand(metaCommand meta) {
		this(meta.getValue());
	}
	@Override
	public metaValue clone() {
		return new metaCommand(this);
	}


	// type
	@Override
	public metaType getType() {
		return COMMAND;
	}


	// get value
	public String getValue() {
		synchronized(lock) {
			return value;
		}
	}
	@Override
	public String getString() {
		return getValue();
	}


	// set value
	public void set(String value) {
		synchronized(lock) {
			this.value = value;
		}
	}


}
