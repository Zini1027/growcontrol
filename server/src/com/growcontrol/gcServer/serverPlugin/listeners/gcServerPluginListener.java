package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent;

public interface gcServerPluginListener {


	public static enum ListenerType{COMMAND, TICK, OUTPUT, INPUT};

	public boolean fire(gcServerEvent event);


}
