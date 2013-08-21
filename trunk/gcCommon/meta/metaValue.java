package com.growcontrol.gcCommon.meta;


public interface metaValue extends java.io.Serializable {

	public metaValue clone();
	public metaType getType();

	public String getString();

}
