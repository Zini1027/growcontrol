package com.growcontrol.gcServer;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandListenerGroup;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;


public class ServerListeners {

	// commands
	private pxnCommandListenerGroup commandListener;
	private ServerCommands serverCommands;


	// init listeners
	public ServerListeners() {
		commandListener = new pxnCommandListenerGroup();
		serverCommands = new ServerCommands();
		commandListener.register(serverCommands);
	}


	// commands
	public void registerCommandListener(pxnCommandsHolder listener) {
		commandListener.register(listener);
	}
	// trigger command
	public boolean triggerCommand(String line) {
		return commandListener.triggerCommandEvent(line);
	}
//	public boolean triggerCommand(pxnCommandEvent event) {
//		if(event == null) throw new NullPointerException("event can't be null!");
//		return commandListener.triggerCommandEvent(event);
//	}


}
