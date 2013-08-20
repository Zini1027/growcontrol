package com.growcontrol.gcCommon.meta.types;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.pxnMetaType;


public class metaEC extends pxnMetaType {
	private static final long serialVersionUID = 7L;

	protected volatile Long value = null;
	protected final Object lock = new Object();


	// new meta object
	public static metaEC newValue(Long value) {
		metaEC meta = new metaEC();
		meta.set(value);
		return meta;
	}
	// new dao (value holder)
	public metaEC() {}
	// type singleton
	public metaEC(String name) {
		super(name);
	}


	// set value
	public void set(Long value) {
		synchronized(lock) {
			// set null
			if(value == null) {
				this.value = null;
				return;
			}
			// set value
			this.value = pxnUtils.MinMax(value.longValue(), 0L, 5000L);
		}
	}
	@Override
	public void set(String value) {
//TODO:
	}


	// get value
	public Long get() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.longValue();
		}
	}
	@Override
	public String toString() {
		Long value = get();
		if(value == null)
			return null;
		return Long.toString(get());
	}


}
