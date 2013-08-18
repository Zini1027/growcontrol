package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.meta.pxnMetaType;


public class metaCommand extends pxnMetaType {
	private static final long serialVersionUID = 7L;

	protected volatile String commandStr = null;
	protected final Object lock = new Object();


	public metaCommand(String name) {
		super(name);
	}
	public metaCommand(String name, String commandStr) {
		super(name);
		set(commandStr);
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
