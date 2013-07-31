package com.growcontrol.gcClient;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandListenerGroup;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;


public class ClientListeners {

	// single instance
	private static ClientListeners listeners = null;

	// client commands
	private ClientCommands commands;


	public static synchronized ClientListeners get() {
		if(listeners == null)
			listeners = new ClientListeners();
		return listeners;
	}
	// init listeners
	private ClientListeners() {
		// client commands listener
		commands = new ClientCommands();
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
