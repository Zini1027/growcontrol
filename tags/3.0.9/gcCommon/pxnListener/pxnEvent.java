package com.growcontrol.gcCommon.pxnListener;


public class pxnEvent {

	// listener priorities
	public static enum EventPriority {
		HIGHEST,
		HIGH,
		NORMAL,
		LOW,
		LOWEST
	}

	private volatile boolean handled = false;


	// event is handled
	public void setHandled() {
		setHandled(true);
	}
	public void setHandled(boolean handled) {
		this.handled = handled;
	}
	public boolean isHandled() {
		return handled;
	}


	// reset for next listener
	public void reset() {}


}
