package com.growcontrol.gcCommon.meta;

import com.growcontrol.gcCommon.pxnUtils;


public class metaVariable extends pxnMeta {
	private static final long serialVersionUID = 7L;

	protected volatile Integer value = null;
	protected volatile Integer override = null;
	protected volatile int min = 0;
	protected volatile int max = 1;


	public metaVariable(String name) {
		super(name);
	}




	// set value
	public void set(Integer value) {
		this.value = value;
	}
	@Override
	public void set(String value) {
		Integer i = pxnUtils.toNumber(value);
		if(i == null) return;
		set(i);
	}


	// get value
	public Integer get() {
		if(override != null)
			return pxnUtils.MinMax((int) override, min, max);
		if(value == null)
			return null;
		return pxnUtils.MinMax((int) value, min, max);
	}
	@Override
	public String toString() {
		Integer v = get();
		if(v == null) return null;
		return Integer.toString(v);
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
