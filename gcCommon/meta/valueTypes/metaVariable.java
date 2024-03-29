package com.growcontrol.gcCommon.meta.valueTypes;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.meta.metaType;
import com.growcontrol.gcCommon.meta.metaValue;
import com.growcontrol.gcCommon.meta.valueFactory;


public class metaVariable extends metaValue {
	private static final long serialVersionUID = 9L;

	// raw value
	protected volatile Integer value = null;
	protected volatile Integer override = null;
	protected volatile int min = 0;
	protected volatile int max = 1;
	protected final Object lock = new Object();


	// static type
	public static final metaType VARIABLE = new metaType("VARIABLE",
		new valueFactory() {
			@Override
			public metaValue newValue() {
				return new metaVariable();
			}
			@Override
			public metaValue newValue(String value) {
				return new metaVariable(value);
			}
	});
	public static void Init() {
		if(VARIABLE == null) System.out.println("Failed to load meta type VARIABLE!");
	}


	// instance
	public metaVariable() {
		set((Integer) null);
	}
	public metaVariable(Integer value) {
		set(value);
	}
	public metaVariable(String value) {
		set(value);
	}
	public metaVariable(metaValue meta) {
		if(meta instanceof metaVariable)
			set( ((metaVariable) meta).getValue() );
		else
			set(meta.getString());
	}
	@Override
	public metaValue clone() {
		return new metaVariable(this);
	}


	// type
	@Override
	public metaType getType() {
		return VARIABLE;
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
		return Integer.toString(i);
	}
	public static String toString(metaVariable meta) {
		if(meta == null)
			return null;
		return meta.getString();
	}
	// get percent
	public Double getPercent() {
		Integer I = getValue();
		if(I == null)
			return null;
		int i = I.intValue();
		i -= min;
		double ma = max;
		double mi = min;
		return ((ma-mi) / i);
	}
	public String getPercentStr() {
		return Double.toString(getPercent())+"%";
	}


	// set value
	public void set(Integer value) {
		synchronized(lock) {
			this.value = pxnUtils.MinMax(
					value.intValue(),
					min,
					max
				);
		}
	}
	@Override
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


//	public void setMinValue(int value) {
//		this.min = value;
//	}
//	public void setMaxValue(int value) {
//		this.max = value;
//	}
//	public void setDisabledValue(int value) {
//		this.disabled = value;
//	}


}
