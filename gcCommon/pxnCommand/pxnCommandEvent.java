package com.growcontrol.gcCommon.pxnCommand;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnEvent.pxnEvent;


public class pxnCommandEvent extends pxnEvent {

	public final pxnCommand command;
	public final String commandRaw;
	public final String commandStr;
	public final List<String> args = new ArrayList<String>();


	public pxnCommandEvent(pxnCommand command, String commandRaw) {
		if(command    == null) throw new NullPointerException("command cannot be null!");
		if(commandRaw == null) throw new NullPointerException("commandRaw cannot be null!");
		if(commandRaw.isEmpty()) throw new NullPointerException("commandRaw cannot be empty!");
		this.command = command;
		this.commandRaw = commandRaw;
		String[] tmp = commandRaw.split(" ");
		if(tmp.length == 1) {
			this.commandStr = commandRaw.trim().toLowerCase();
		} else {
			this.commandStr = tmp[0].trim().toLowerCase();
			for(int i = 1; i < tmp.length; i++)
				this.args.add(tmp[i]);
		}
	}


//	public String getRaw() {
//		return commandRaw;
//	}
//	public String get(int index) {
//		if(index <= -1)
//			return commandStr;
//		if(index < args.size())
//			return args.get(index);
//		return null;
//	}
//	public String toString() {
//		return this.get(-1);
//	}


}
