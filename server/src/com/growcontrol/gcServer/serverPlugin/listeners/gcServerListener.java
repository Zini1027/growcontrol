package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent.EventPriority;


public class gcServerListener {

	public static enum ListenerType{COMMAND, TICK, OUTPUT, INPUT};
	protected EventPriority priority = EventPriority.NORMAL;


	// event priority
	protected void setPriority(EventPriority priority) {
		if(priority == null) throw new NullPointerException();
		this.priority = priority;
	}
	public boolean priorityEquals(EventPriority priority) {
		if(this.priority == null) throw new NullPointerException();
		if(     priority == null) throw new NullPointerException();
		return this.priority.equals(priority);
	}


	public boolean doEvent(gcServerEvent event) {
		gcServer.log.severe("Event not handled!");
		return false;
	}


}
