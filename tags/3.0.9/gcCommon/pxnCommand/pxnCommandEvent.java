package com.growcontrol.gcCommon.pxnCommand;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnListener.pxnEvent;


public class pxnCommandEvent extends pxnEvent {

	// raw command
	public final String raw;
	// command string (alias resolved)
	public final String commandStr;
	// command object
	public final pxnCommand command;
	// command arguments
	public final List<String> args = new ArrayList<String>();


	// new command event - newEvent(raw, commandListener.getCommand(raw))
	public static pxnCommandEvent newEvent(String raw, pxnCommand command) {
		if(raw == null)   throw new NullPointerException("raw cannot be null!");
		if(raw.isEmpty()) throw new NullPointerException("raw cannot be empty!");
		if(command == null) throw new NullPointerException("command cannot be null!");
		// new event
		return new pxnCommandEvent(raw, command);
	}
	protected pxnCommandEvent(String raw, pxnCommand command) {
		if(raw == null)   throw new NullPointerException("raw cannot be null!");
		if(raw.isEmpty()) throw new NullPointerException("raw cannot be empty!");
		if(command == null) throw new NullPointerException("command cannot be null!");
		this.raw = raw;
		this.commandStr = command.commandStr;
		this.command = command;
		// args
		args.clear();
		String[] tmp = raw.split(" ");
		if(tmp.length > 1) {
			for(int i = 1; i < tmp.length; i++) {
				if(tmp[i].isEmpty()) continue;
				this.args.add(tmp[i]);
			}
		}
	}


	public boolean equals(String compareStr) {
		if(compareStr == null) return false;
		return compareStr.trim().toLowerCase().equals( getCommandStr() );
	}
	public String getCommandStr() {
		return this.commandStr;
	}


	public String[] getArgs() {
		return (String[]) args.toArray(new String[0]);
	}


}
