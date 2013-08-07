package com.growcontrol.gcCommon.meta;

import com.growcontrol.gcCommon.pxnUtils;


public class metaEC extends pxnMeta {
	private static final long serialVersionUID = 7L;

	protected volatile Long value = null;


	public metaEC(String name) {
		super(name);
	}
	public metaEC(String name, String value) {
		super(name);
		set(value);
	}
	public metaEC(String name, Long value) {
		super(name);
		set(value);
	}


	// set value
	public void set(Long value) {
		if(value == null) {
			this.value = null;
			return;
		}
		this.value = pxnUtils.MinMax((long) value, 0, 5000);
	}
	@Override
	public void set(String value) {
//TODO:
	}


	// get value
	public Long get() {
		return value;
	}
	@Override
	public String toString() {
		if(value == null) return null;
		return Long.toString(value);
	}


}
