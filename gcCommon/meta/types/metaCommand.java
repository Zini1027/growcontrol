package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.meta.pxnMetaType;


public class metaCommand extends pxnMetaType {
	private static final long serialVersionUID = 7L;

	protected volatile String commandStr = null;


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
		this.commandStr = value;
	}


	// get value
	@Override
	public String toString() {
		return commandStr;
	}


}
