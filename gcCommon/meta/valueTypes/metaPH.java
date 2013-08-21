package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaPH implements metaValue {
	private static final long serialVersionUID = 9L;

	// raw value
	protected volatile Integer value = null;
	protected final Object lock = new Object();


	// static type
	public static final metaType PH = new metaType("PH",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaPH();
			}
	});


	// instance
	public metaPH() {
		set((Integer) null);
	}
	public metaPH(Integer value) {
		set(value);
	}
	public metaPH(metaPH meta) {
		this(meta.getValue());
	}
	@Override
	public metaValue clone() {
		return new metaPH(this);
	}


	// type
	@Override
	public metaType getType() {
		return PH;
	}


	// get value
	public Integer getValue() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.intValue();
		}
	}
	@Override
	public String getString() {
		Integer i = getValue();
		if(i == null)
			return null;
		return Integer.toString(i)+"pH";
	}


	// set value
	public void set(Integer value) {
		synchronized(lock) {
			this.value = pxnUtils.MinMax(
					value.intValue(),
					0,
					14
				);
		}
	}
	public void set(String value) {
		if(value == null || value.isEmpty()) {
			set((Integer) null);
			return;
		}
		value = value.replace("p", "").replace("h", "").trim();
		Integer i = null;
		try {
			i = Integer.valueOf(value);
		} catch (Exception ignore) {}
		set(i);
	}




}
