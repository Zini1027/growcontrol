package com.growcontrol.gcCommon.pxnCommand;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnListener.pxnEvent;


public class pxnCommandEvent extends pxnEvent {

	public final String commandRaw;
	public final String commandStr;
	public final List<String> args = new ArrayList<String>();


	// new command event
	public static pxnCommandEvent newEvent(String commandRaw, pxnCommandsHolder commandListener) {
		if(commandRaw == null)      throw new NullPointerException("commandRaw cannot be null!");
		if(commandRaw.isEmpty())    throw new NullPointerException("commandRaw cannot be empty!");
		if(commandListener == null) throw new NullPointerException("commandListener cannot be null!");
		// check for alias, return command
		String commandStr = commandListener.getCommand(commandRaw);
		// not found
		if(commandStr == null) return null;
		// new event
		return new pxnCommandEvent(commandRaw, commandStr);
	}
	protected pxnCommandEvent(String commandRaw, String commandStr) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null!");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty!");
		if(commandRaw == null)   throw new NullPointerException("commandRaw cannot be null!");
		if(commandRaw.isEmpty()) throw new NullPointerException("commandRaw cannot be empty!");
		this.commandRaw = commandRaw;
		this.commandStr = commandStr.trim().toLowerCase();
		String[] tmp = commandRaw.split(" ");
		// args
		if(tmp.length > 1) {
			for(int i = 1; i < tmp.length; i++) {
				if(tmp[i].isEmpty()) continue;
				this.args.add(tmp[i]);
			}
		}
	}


	public boolean equals(String compareStr) {
		if(compareStr == null) return false;
		if(compareStr.trim().toLowerCase() == getCommandStr())
			return true;
		return false;
	}
	public String getCommandStr() {
		return this.commandStr;
	}


	public String[] getArgsArray() {
		return (String[]) args.toArray(new String[0]);
	}


}
