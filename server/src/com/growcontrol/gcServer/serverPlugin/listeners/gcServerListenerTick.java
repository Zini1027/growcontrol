package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcCommon.pxnListener.pxnEvent;


public abstract class gcServerListenerTick extends gcServerListener {


	// do event
	public abstract boolean onTick(pxnEvent event);
	public boolean doEvent(pxnEvent event) {
		if(event == null) throw new NullPointerException("event cannot be null");
		return onTick(event);
	}


}
