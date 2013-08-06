package com.growcontrol.gcCommon.meta;


public class DataEC implements DataType {

	protected long value;


	@Override
	public String toString() {
		return toString(null);
	}
	@Override
	public String toString(String arg) {
		return Long.toString(this.value);
	}


	public void set(long value) {
		this.value = value;
	}


}
