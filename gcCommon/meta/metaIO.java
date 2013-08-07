package com.growcontrol.gcCommon.meta;

import com.growcontrol.gcCommon.pxnUtils;


public class metaIO extends pxnMeta {
	private static final long serialVersionUID = 7L;

	protected volatile Boolean value = null;


	public metaIO(String name) {
		super(name);
	}
	public metaIO(String name, String value) {
		super(name);
		set(value);
	}
	public metaIO(String name, Boolean value) {
		super(name);
		set(value);
	}


	// set value
	public void set(Boolean value) {
		this.value = value;
	}
	@Override
	public void set(String value) {
		Boolean b = pxnUtils.toBoolean(value);
		if(b == null) return;
		set(b);
	}


	// get value
	public Boolean get() {
		return value;
	}
	@Override
	public String toString() {
		return Boolean.toString(value);
	}


}
