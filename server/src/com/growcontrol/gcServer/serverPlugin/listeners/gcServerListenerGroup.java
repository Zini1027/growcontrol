package com.growcontrol.gcServer.serverPlugin.listeners;

import com.growcontrol.gcServer.Main;
import com.growcontrol.gcCommon.pxnListener.pxnListenerGroup;


public class gcServerListenerGroup extends pxnListenerGroup {

	public static enum ListenerType{SERVER, COMMAND, TICK, OUTPUT, INPUT};
	protected final ListenerType listenerType;


	public gcServerListenerGroup(ListenerType type) {
		if(type == null) throw new NullPointerException("listenerType cannot be null");
		this.listenerType = type;
	}


	// register listener
	public void registerCommandListener(gcServerListenerCommand listener) {
		if(listener == null) throw new NullPointerException("listener cannot be null");
		TypeMustEqual(listenerType, listener);
		Main.getLogger().debug("Registered listener: "+listener.toString());
		listeners.add(listener);
	}


	// listener type equals
	public static void TypeMustEqual(ListenerType listenerType, gcServerListener listener) {
		if(!TypeEquals(listenerType, listener)) {
			Main.getLogger().severe("Invalid listener type!");
			throw new IllegalArgumentException("Invalid listener type!");
		}
	}
	public static boolean TypeEquals(ListenerType listenerType, gcServerListener listener) {
		if(listenerType == null) return true;
		// commands listener
		if(listener instanceof gcServerListenerCommand)
			return listenerType.equals(ListenerType.COMMAND);
		//
//		else if(listener instanceof gcServerListenerSomethingelse)
//			return listenerType.equals(ListenerType);
		Main.getLogger().severe("Unknown listener type!");
		throw new IllegalArgumentException("Unknown listener type!");
	}


}
