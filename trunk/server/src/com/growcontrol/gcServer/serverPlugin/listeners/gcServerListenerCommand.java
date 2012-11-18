package com.growcontrol.gcServer.serverPlugin.listeners;

import java.util.HashMap;

import com.growcontrol.gcServer.serverPlugin.commands.gcCommand;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;

public abstract class gcServerListenerCommand extends gcServerListener {

	protected HashMap<String, gcCommand> commands = new HashMap<String, gcCommand>();


	// do event
	public abstract boolean onCommand(gcServerEventCommand event);
	public boolean doEvent(gcServerEventCommand event) {
		if(event == null) throw new NullPointerException();
		event.hasCommand(hasCommand(event.getCommandStr()));
		return onCommand(event);
	}


	// new command
	public gcCommand add(String name) {
		return addCommand(name);
	}
	public gcCommand addCommand(String name) {
		if(name == null) throw new NullPointerException();
		if(commands.containsKey(name)) {
			return commands.get(name);
		} else {
			gcCommand command = new gcCommand(name);
			commands.put(name, command);
			return command;
		}
	}


	// has command/alias
	public boolean hasCommand(String name) {
		for(gcCommand command : commands.values())
			if(command.hasCommand(name))
				return true;
		return false;
	}


}
