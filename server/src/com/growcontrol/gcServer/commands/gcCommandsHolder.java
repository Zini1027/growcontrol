package com.growcontrol.gcServer.commands;

import java.util.HashMap;

import com.growcontrol.gcServer.gcServer;

public class gcCommandsHolder {

	private static final HashMap<String, gcCommand> commands = new HashMap<String, gcCommand>();


	// add a command
	public gcCommand addCommand(String name) {
		gcCommand command;
		if(commands.containsKey(name)) {
			gcServer.log.warning("Command already registered: "+name);
			command = commands.get(name);
		} else {
			command = new gcCommand(name);
			commands.put(name, command);
		}
		return command;
	}


	// has command
	public boolean hasCommand(String command) {
		return commands.containsKey(command);
	}
	@deprecated
	public boolean hasCommandAlias(String command) {
		return (getCommandAlias(command) != null);
	}


	// get command
	public gcCommand getCommand(String command) {
		if(!hasCommand(command)) return null;
		return commands.get(command);
	}
	public gcCommand getCommandAlias(String command) {
		for(gcCommand com : commands.values())
			if(com.hasAlias(command))
				return com;
		return null;
	}


}
