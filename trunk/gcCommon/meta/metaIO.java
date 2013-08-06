package com.growcontrol.gcCommon.meta;


public class DataIO implements DataType {

	protected Boolean value = null;


	@Override
	public String toString() {
		return toString(null);
	}
	@Override
	public String toString(String arg) {
		return Boolean.toString(this.value);
	}


	public void set(Boolean value) {
		this.value = value;
	}


}
