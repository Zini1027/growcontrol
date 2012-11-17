package com.growcontrol.gcServer.serverPlugin.listeners;

public interface gcServerListenerInput {


	public abstract boolean onInput(gcServerEventInput event);


	public boolean doEvent(gcServerEventInput event) {
		return onInput(event);
	}


}
