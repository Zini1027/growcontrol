package com.growcontrol.gcServer.serverPlugin.events;

public class gcServerEventCommand extends gcServerEvent {

	protected final String commandStr;
	protected final String[] args;


	public gcServerEventCommand(String commandStr, String[] args) {
		this.commandStr = commandStr;
		this.args = args;
	}


	public String getCommandStr() {
		return commandStr;
	}
	public String getArg(int index) {
		return args[index];
	}


	public boolean equals(String compare) {
		if(compare == null) return false;
		return compare.equals(commandStr);
	}


}
