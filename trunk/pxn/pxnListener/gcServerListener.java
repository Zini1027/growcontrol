package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent.EventPriority;


public class gcServerListener {

	public static enum ListenerType{COMMAND, TICK, OUTPUT, INPUT};
	protected EventPriority priority = EventPriority.NORMAL;


	// event priority
	protected void setPriority(EventPriority priority) {
		if(priority == null) throw new NullPointerException("priority cannot be null");
		this.priority = priority;
	}
	public boolean priorityEquals(EventPriority priority) {
		if(this.priority == null) throw new NullPointerException("this.priority cannot be null");
		if(     priority == null) throw new NullPointerException("priority cannot be null");
		return this.priority.equals(priority);
	}


	public boolean doEvent(gcServerEvent event) {
		if(event == null) throw new NullPointerException("event cannot be null");
		if(event instanceof gcServerEventCommand)
			return ((gcServerListenerCommand) this).doEvent( (gcServerEventCommand) event );
//		else if(event instanceof gcServerEventSomethingelse)
//			return ((gcServerListenerSomethingelse) this).doEvent( (gcServerEventSomethingelse) event );
		gcServer.log.severe("Event not handled!");
		return false;
	}


}
