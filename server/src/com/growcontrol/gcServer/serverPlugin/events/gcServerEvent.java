package com.growcontrol.gcServer.serverPlugin.events;

import com.growcontrol.gcServer.gcServer;

public abstract class gcServerEvent {


	protected boolean handled = false;


	public boolean isHandled() {
		return handled;
	}
	public void setHandled() {
		handled = true;
		gcServer.log.debug("Event handled: ???");
	}


}
