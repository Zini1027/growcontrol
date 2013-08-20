package com.growcontrol.gcCommon.pxnCommand;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcCommon.pxnListener.pxnEvent;
import com.growcontrol.gcCommon.pxnListener.pxnListener;


public abstract class pxnCommandsHolder extends pxnListener {

	// commands
	protected HashMap<String, pxnCommand> commands = new HashMap<String, pxnCommand>();


	public pxnCommandsHolder() {
		initCommands();
	}
	protected abstract void initCommands();


	// add command
	public pxnCommand addCommand(String commandStr) {
		if(commandStr == null) return null;
		commandStr = commandStr.toLowerCase();
		pxnCommand command = null;
		synchronized(commands) {
			// command exists
			if(commands.containsKey(commandStr))
				return commands.get(commandStr);
			// new command
			command = new pxnCommand(commandStr);
			commands.put(commandStr, command);
		}
		return command;
	}
	public pxnCommand addCommand(String commandStr, String usageStr) {
		pxnCommand command = addCommand(commandStr);
		if(command == null) return null;
		if(usageStr == null || usageStr.isEmpty())
			usageStr = null;
		command.usageStr = usageStr;
		return command;
	}
	// add command alias
	public void addAlias(String commandStr, String aliasStr) {
		pxnCommand command = addCommand(commandStr);
		command.aliases.add(aliasStr);
	}


	// get command
	public pxnCommand getCommand(String commandRaw) {
		String commandStr = FindCommand(commandRaw);
		if(commandStr == null || commandStr.isEmpty()) return null;
		return commands.get(commandStr);
	}
	// get command from raw string
	public String FindCommand(String commandRaw) {
		if(commandRaw == null) return null;
		commandRaw = PrepCommand(commandRaw);
		// check command names
		for(Entry<String, pxnCommand> entry : commands.entrySet())
			if(entry.getKey().equals(commandRaw))
				return entry.getKey();
		// check command aliases
		for(Entry<String, pxnCommand> entry : commands.entrySet())
			if(entry.getValue().hasAlias(commandRaw))
				return entry.getValue().commandStr;
		// null if not found
		return null;
	}
	private String PrepCommand(String commandRaw) {
		// trim to first word
		if(commandRaw.contains(" "))
			commandRaw = commandRaw.substring(0, commandRaw.indexOf(" "));
		return commandRaw.trim().toLowerCase();
	}


	// command event
	protected abstract boolean onCommand(pxnCommandEvent command);
	@Override
	public boolean onEvent(pxnEvent event) {
		if(!(event instanceof pxnCommandEvent)) throw new IllegalArgumentException("Not a command event!");
		pxnCommandEvent command = (pxnCommandEvent) event;
		if(!this.commands.containsKey(command.commandStr))
			return false;
		return onCommand(command);
	}


}
