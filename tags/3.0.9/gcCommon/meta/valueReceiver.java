package com.growcontrol.gcCommon.meta;


// meta input listener
public interface valueReceiver {

	// input device name
	public String getReceiverName();

	// input event
	public boolean onProcessMeta(metaValue meta);

}
