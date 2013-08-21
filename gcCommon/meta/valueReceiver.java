package com.growcontrol.gcCommon.meta;


// meta input listener
public interface valueReceiver {

	// input device name
	public String getName();

	// input event
	public boolean onProcess(metaValue meta);

}
