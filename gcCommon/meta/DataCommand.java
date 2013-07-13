package com.growcontrol.gcCommon.meta;


public class DataCommand implements DataType {

	protected String label;
	protected String[] args;


	public DataCommand() {
		this("");
	}
	public DataCommand(String commandStr) {
		this.label = commandStr;
		this.args = commandStr.split(" ");
	}


	@Override
	public String toString() {
		return toString(null);
	}
	@Override
	public String toString(String arg) {
		return label;
	}


	public String getLabel() {
		return this.label;
	}
	public boolean labelEquals(String label) {
		if(label == null)
			return false;
		return label.equals(this.label);
	}


	public String[] getArgs() {
		return this.args;
	}


}
