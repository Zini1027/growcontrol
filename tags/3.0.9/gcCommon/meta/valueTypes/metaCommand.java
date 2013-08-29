package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaCommand extends metaValue {
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
			@Override
			public metaValue newValue(String value) {
				return new metaCommand(value);
			}
	});
	public static void Init() {
		if(COMMAND == null) System.out.println("Failed to load meta type COMMAND!");
	}


	// instance
	public metaCommand() {
		set((String) null);
	}
	public metaCommand(String value) {
		set(value);
	}
	public metaCommand(metaValue meta) {
		if(meta instanceof metaCommand)
			set( ((metaCommand) meta).getValue() );
		else
			set(meta.getString());
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
	public static String toString(metaCommand meta) {
		if(meta == null)
			return null;
		return meta.getString();
	}


	// set value
	@Override
	public void set(String value) {
		synchronized(lock) {
			this.value = value;
		}
	}


}
