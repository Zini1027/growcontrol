package com.growcontrol.gcServer.serverPlugin.listeners;

public abstract class gcServerListenerDevice extends gcServerListener {


	public abstract boolean onDevice(gcServerEventDevice event);


	public boolean doEvent(gcServerEventDevice event) {
		return onDevice(event);
	}


}
