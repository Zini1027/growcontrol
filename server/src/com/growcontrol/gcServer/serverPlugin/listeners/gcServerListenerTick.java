package com.growcontrol.gcServer.serverPlugin.listeners;

public abstract class gcServerListenerTick extends gcServerListener {


	public abstract boolean onTick(gcServerEventTick event);


	public boolean doEvent(gcServerEventTick event) {
		return onTick(event);
	}


}
