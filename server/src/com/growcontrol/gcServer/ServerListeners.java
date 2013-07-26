package com.growcontrol.gcServer;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandListenerGroup;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;


public final class ServerListeners {

	private static ServerListeners listeners = null;

//	// commands
//	private pxnCommandListenerGroup commandListener;
	// server commands
	private ServerCommands commands;


	public static synchronized ServerListeners get() {
		if(listeners == null)
			listeners = new ServerListeners();
		return listeners;
	}


	// init listeners
	private ServerListeners() {
		// commands listener
		commands = new ServerCommands();
		register(commands);
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	// commands holder
	public void register(pxnCommandsHolder listener) {
		pxnCommandListenerGroup.get().register(listener);
	}
	// trigger command
	public boolean triggerCommand(String line) {
		return pxnCommandListenerGroup.get().triggerCommandEvent(line);
	}


}
