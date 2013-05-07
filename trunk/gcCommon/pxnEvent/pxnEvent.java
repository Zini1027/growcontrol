package com.gcCommon.pxnEvent;


public class pxnEvent {

	// listener priorities
	public static enum EventPriority {
		HIGHEST,
		HIGH,
		NORMAL,
		LOW,
		LOWEST
	}


	// event is handled
	protected boolean handled = false;
	public void setHandled() {
		handled = true;
	}
	public boolean isHandled() {
		return handled;
	}


}
