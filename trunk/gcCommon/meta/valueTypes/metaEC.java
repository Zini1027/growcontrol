package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaEC implements metaValue {
	private static final long serialVersionUID = 9L;

	// raw value
	protected volatile Integer value = null;
	protected final Object lock = new Object();


	// static type
	public static final metaType EC = new metaType("EC",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaEC();
			}
	});


	// instance
	public metaEC() {
		set((Integer) null);
	}
	public metaEC(Integer value) {
		set(value);
	}
	public metaEC(metaEC meta) {
		this(meta.getValue());
	}
	@Override
	public metaValue clone() {
		return new metaEC(this);
	}


	// type
	@Override
	public metaType getType() {
		return EC;
	}


	// get value
	public Integer getValue() {
		synchronized(lock) {
			if(value == null)
				return null;
			return value.intValue();
		}
	}
	public Integer getPPM() {
		return getValue();
	}
	@Override
	public String getString() {
		Integer i = getPPM();
		if(i == null)
			return null;
		return Integer.toString(i)+"ppm";
	}


	// set value
	public void set(Integer value) {
		synchronized(lock) {
			this.value = pxnUtils.MinMax(
				value.intValue(),
				0,
				5000
			);
		}
	}
	public void set(String value) {
		if(value == null || value.isEmpty()) {
			set((Integer) null);
			return;
		}
		Integer i = null;
		try {
			i = Integer.valueOf(value);
		} catch (Exception ignore) {}
		set(i);
	}


}
