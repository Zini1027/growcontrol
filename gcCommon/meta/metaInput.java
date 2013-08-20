package com.growcontrol.gcCommon.meta;


// meta input listener
public interface metaInput {

	// input device name
	public String getName();

	// input event
	public boolean onInput(pxnMetaType meta);

}
