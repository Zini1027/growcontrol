package com.growcontrol.gcServer.serverPlugin.listeners;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent.EventPriority;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListener.ListenerType;

public class gcServerListenerHolder {

	protected final ListenerType listenerType;
	protected List<gcServerListener> listeners = new ArrayList<gcServerListener>();


	public gcServerListenerHolder(ListenerType listenerType) {
		if(listenerType == null) throw new NullPointerException();
		this.listenerType = listenerType;
	}


	// register new listener
	public void registerListener(gcServerListener listener) {
		if(listener == null) throw new NullPointerException();
		TypeMustEqual(listenerType, listener);
gcServer.log.debug("Registered listener");
		listeners.add(listener);
	}


	// trigger event
	public boolean triggerEvent(gcServerEvent event, EventPriority onlyPriority) {
		if(event == null) throw new NullPointerException();
		if(onlyPriority == null) throw new NullPointerException();
		for(gcServerListener listener : listeners)
			if(listener.priorityEquals(onlyPriority))
				if(listener.doEvent(event))
					event.setHandled();
		return event.isHandled();
	}


	// listener type equals
	public static void TypeMustEqual(ListenerType listenerType, gcServerListener listener) {
		if(!TypeEquals(listenerType, listener)) {
			gcServer.log.severe("Invalid listener type!");
//TODO: throw an exception!
			throw new NullPointerException();
		}
	}
	public static boolean TypeEquals(ListenerType listenerType, gcServerListener listener) {
		if(listenerType == null) return true;
		// commands listener
		if(listener instanceof gcServerListenerCommand)
			return listenerType.equals(ListenerType.COMMAND);
//		//
//		else if(listener instanceof gcServerListenerSomethingelse)
//			return listenerType.equals(ListenerType);
		gcServer.log.severe("Unknown listener type!");
//TODO: throw an exception here
		throw new NullPointerException();
	}


}
