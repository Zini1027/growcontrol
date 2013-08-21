package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.meta.metaType;


public class metaCommand extends metaType {
	private static final long serialVersionUID = 7L;

	protected volatile String commandStr = null;
	protected final Object lock = new Object();


	// new meta object
	public static metaCommand newValue(String value) {
		metaCommand meta = new metaCommand();
		meta.set(value);
		return meta;
	}
	// new dao (value holder)
	public metaCommand() {}
	// type singleton
	public metaCommand(String name) {
		super(name);
	}


	// set value
	@Override
	public void set(String value) {
		synchronized(lock) {
			this.commandStr = value;
		}
	}


	// get value
	@Override
	public String toString() {
		synchronized(lock) {
			return commandStr;
		}
	}


}
