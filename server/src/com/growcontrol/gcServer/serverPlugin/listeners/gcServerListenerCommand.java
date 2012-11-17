package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;

public abstract class gcServerListenerCommand extends gcServerListener {


	public abstract boolean onCommand(gcServerEventCommand event);


	public boolean doEvent(gcServerEventCommand event) {
		return onCommand(event);
	}



}
