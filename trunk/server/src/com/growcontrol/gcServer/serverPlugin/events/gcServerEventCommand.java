package com.growcontrol.gcServer.serverPlugin.events;

import com.growcontrol.gcCommon.pxnCommand.pxnCommand;
import com.growcontrol.gcCommon.pxnParser.pxnParser;


public class gcServerEventCommand extends gcServerEvent {

	protected pxnCommand command = null;
	protected final pxnParser parser;


	public gcServerEventCommand(String commandStr) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty");
		this.parser = new pxnParser(commandStr, ' ');
	}


	// command object
	public pxnCommand getCommand() {
		return command;
	}
	public void setCommand(pxnCommand command) {
		this.command = command;
	}
	public boolean hasCommand() {
		return (command != null);
	}


	// get parser
	public pxnParser getLine() {
		return parser;
	}


//	// command string
//	public String getCommandStr() {
//		return commandStr;
//	}
//	public String getArg(int index) {
//		return args[index];
//	}
//	public String[] getArgs() {
//		return args;
//	}


//	public boolean equals(String compare) {
//		if(compare == null) return false;
//		return compare.equals(commandStr);
//	}


}
