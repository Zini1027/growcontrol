package com.growcontrol.gcpromptdisplay;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListenerCommand;
import com.poixson.pxnCommand.pxnCommand;
import com.poixson.pxnEvent.pxnEvent.EventPriority;


public class CommandsListener extends gcServerListenerCommand {


	public CommandsListener() {
		setPriority(EventPriority.NORMAL);
		// register commands
		add("promptdisplay")
			.addAlias("prompt")
			.setUsage("");
	}


	@Override
	public boolean onCommand(gcServerEventCommand event) {
		if(event.isHandled())   return false;
		if(!event.hasCommand()) return false;
		pxnCommand command = event.getCommand();
gcPromptDisplay.log.severe("gcPromptDisplay Command: "+command.toString());
		return true;
	}


}
