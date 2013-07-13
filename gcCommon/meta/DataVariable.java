package com.growcontrol.gcCommon.meta;

import com.growcontrol.gcCommon.pxnUtils;


public class DataVariable implements DataType {

	protected Integer value = null;
	protected int min = 0;
	protected int max = 1;
	protected int disabled = -1;


	public int getValue() {
		if(this.value == null)
			return this.disabled;
		return pxnUtils.MinMax((int) this.value, this.min, this.max);
	}
	@Override
	public String toString() {
		return toString(null);
	}
	@Override
	public String toString(String arg) {
		return Integer.toString(getValue());
	}


	public void set(Integer value) {
		this.value = value;
	}


	public void setMinValue(int value) {
		this.min = value;
	}
	public void setMaxValue(int value) {
		this.max = value;
	}
	public void setDisabledValue(int value) {
		this.disabled = value;
	}


}
