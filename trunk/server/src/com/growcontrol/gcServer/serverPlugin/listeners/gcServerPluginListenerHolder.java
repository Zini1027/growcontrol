package com.growcontrol.gcServer.serverPlugin.listeners;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListener.ListenerType;

public class gcServerPluginListenerHolder {

	ListenerType listenerType = null;
	List<gcServerPluginListener> listeners = new ArrayList<gcServerPluginListener>();


	public gcServerPluginListenerHolder(ListenerType type) {
		listenerType = type;
	}


	public void registerListener(gcServerPluginListener listener) {
gcServer.log.debug("Registered listener");
		listeners.add(listener);
	}
	public boolean fireListeners(gcServerEvent event) {
		for(gcServerPluginListener listener : listeners)
			if(listener.fire(event))
				event.setHandled();
		return false;
	}


}
