package com.growcontrol.gcCommon.meta;

import com.growcontrol.gcCommon.pxnUtils;


public class DataVariable implements DataType {

	private Integer value = null;
	private int min = 0;
	private int max = 1;
	private int disabled = -1;


	public int get() {
		if(this.value == null)
			return this.disabled;
		return pxnUtils.MinMax((int) this.value, this.min, this.max);
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
