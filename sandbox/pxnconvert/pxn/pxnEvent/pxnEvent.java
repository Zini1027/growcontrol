package com.poixson.pxnEvent;

public class pxnEvent {

//	public static enum EventPriority {LOWEST, LOW, NORMAL, HIGH, HIGHEST}
	public static enum EventPriority {
		HIGHEST,
		HIGH,
		NORMAL,
		LOW,
		LOWEST
	}

	protected boolean handled = false;


	public boolean isHandled() {
		return handled;
	}
	public void setHandled() {
		handled = true;
	}


}
