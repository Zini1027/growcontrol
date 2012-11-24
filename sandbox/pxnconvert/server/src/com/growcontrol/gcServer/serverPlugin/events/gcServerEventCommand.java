package com.growcontrol.gcServer.serverPlugin.events;

import com.growcontrol.gcServer.serverPlugin.commands.gcCommand;


public class gcServerEventCommand extends gcServerEvent {

	protected gcCommand command = null;
	protected final String commandStr;
	protected final String[] args;


	public gcServerEventCommand(String commandStr, String[] args) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty");
		if(args == null)         throw new NullPointerException("args cannot be null");
		this.commandStr = commandStr;
		this.args = args;
	}


	// command object
	public gcCommand getCommand() {
		return command;
	}
	public void setCommand(gcCommand command) {
		this.command = command;
	}
	public boolean hasCommand() {
		return (command != null);
	}


	// command string
	public String getCommandStr() {
		return commandStr;
	}
	public String getArg(int index) {
		return args[index];
	}
	public String[] getArgs() {
		return args;
	}


//	public boolean equals(String compare) {
//		if(compare == null) return false;
//		return compare.equals(commandStr);
//	}


}
