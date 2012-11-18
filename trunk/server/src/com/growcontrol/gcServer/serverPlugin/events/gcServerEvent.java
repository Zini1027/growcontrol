package com.growcontrol.gcServer.serverPlugin.events;


public class gcServerEvent {

	public static enum EventPriority {LOWEST, LOW, NORMAL, HIGH, HIGHEST}

	protected boolean handled = false;


	public boolean isHandled() {
		return handled;
	}
	public void setHandled() {
		handled = true;
	}


}
