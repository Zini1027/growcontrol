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
		// command exists
		if(commands.containsKey(commandStr))
			return commands.get(commandStr);
		// new command
		pxnCommand command = new pxnCommand(commandStr);
		commands.put(commandStr, command);
		return command;
	}
	public pxnCommand addCommand(String commandStr, String usageStr) {
		pxnCommand command = addCommand(commandStr);
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


	// local shorthand
	protected pxnCommand add(String commandStr) {
		return addCommand(commandStr);
	}
	protected pxnCommand add(String commandStr, String usageStr) {
		return addCommand(commandStr, usageStr);
	}


	// get command from raw string
	public String getCommand(String commandRaw) {
		if(commandRaw == null) return null;
		// trim to first word
		if(commandRaw.contains(" "))
			commandRaw = commandRaw.substring(0, commandRaw.indexOf(" "));
		commandRaw = commandRaw.trim().toLowerCase();
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
