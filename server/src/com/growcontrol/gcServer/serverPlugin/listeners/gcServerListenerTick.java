package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEventTick;

public abstract class gcServerListenerTick extends gcServerListener {


	// do event
	public abstract boolean onTick(gcServerEventTick event);
	public boolean doEvent(gcServerEventTick event) {
		if(event == null) throw new NullPointerException();
		return onTick(event);
	}


}
