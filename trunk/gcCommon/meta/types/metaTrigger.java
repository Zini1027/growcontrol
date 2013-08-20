package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.meta.pxnMetaType;


public class metaTrigger extends pxnMetaType {
	private static final long serialVersionUID = 7L;


	// new meta object
	public static metaTrigger newValue() {
		metaTrigger meta = new metaTrigger();
		return meta;
	}
	// new dao (value holder)
	public metaTrigger() {}
	// type singleton
	public metaTrigger(String name) {
		super(name);
	}


	// set value
	@Override
	public void set(String value) {}


	// get value
	@Override
	public String toString() {
		return null;
	}


}
