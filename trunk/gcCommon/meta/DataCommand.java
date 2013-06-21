package com.growcontrol.gcCommon.meta;


public class DataCommand {

	private String label;
	private String[] args;


	public DataCommand() {
		this("");
	}
	public DataCommand(String commandStr) {
		this.label = commandStr;
		this.args = commandStr.split(" ");
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
