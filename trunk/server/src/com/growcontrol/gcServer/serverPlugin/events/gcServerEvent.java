package com.growcontrol.gcServer.serverPlugin.events;

import com.growcontrol.gcServer.gcServer;

public class gcServerEvent {

	public static enum EventPriority {LOWEST, LOW, NORMAL, HIGH, HIGHEST}

	protected boolean handled = false;


	public boolean isHandled() {
		return handled;
	}
	public void setHandled() {
		handled = true;
		gcServer.log.debug("Event handled: ???");
	}


}
