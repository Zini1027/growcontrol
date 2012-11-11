package com.growcontrol.gcServer.commands;

import java.util.HashMap;

import com.growcontrol.gcServer.gcServer;

public class gcCommandsHolder {

	private HashMap<String, gcCommand> commands = new HashMap<String, gcCommand>();


	// add a command
	public gcCommand addCommand(String name) {
		if(name == null) throw new NullPointerException();
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


	// get command by name or alias
	public gcCommand getCommandOrAlias(String commandStr) {
		if(commandStr == null) throw new NullPointerException();
		if(commands.containsKey(commandStr))
			return commands.get(commandStr);
		for(gcCommand command : commands.values())
			if(command.hasAlias(commandStr))
				return command;
		return null;
	}
//	public boolean hasCommandOrAlias(String commandStr) {
//		return (getCommandOrAlias(commandStr) == null);
//	}


}
