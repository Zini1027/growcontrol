package com.growcontrol.gcServer.serverPlugin.listeners;

public interface gcServerListenerOutput {


	public abstract boolean onOutput(gcServerEventOutput event);


	public boolean doEvent(gcServerEventOutput event) {
		return onOutput(event);
	}


}
