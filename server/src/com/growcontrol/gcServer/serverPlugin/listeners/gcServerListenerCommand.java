package com.growcontrol.gcServer.serverPlugin.listeners;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.poixson.pxnCommand.pxnCommand;


public abstract class gcServerListenerCommand extends gcServerListener {

	protected HashMap<String, pxnCommand> commands = new HashMap<String, pxnCommand>();


	// do event
	public abstract boolean onCommand(gcServerEventCommand event);
//	public boolean doEvent(gcServerEventCommand event) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//System.out.println("ONCOMMAND "+event.toString());
//		setHasCommand(event);
//		event.hasCommand(hasCommand(event.getCommandStr()));
//		return onCommand(event);
//	}


	// new command
	public pxnCommand add(String name) {
		return addCommand(name);
	}
	public pxnCommand addCommand(String name) {
		if(name == null) throw new NullPointerException("name cannot be null");
		if(commands.containsKey(name)) {
			return commands.get(name);
		} else {
			pxnCommand command = new pxnCommand(name);
			commands.put(name, command);
			return command;
		}
	}


	// find command/alias
	protected void setHasCommand(gcServerEventCommand event) {
		if(event == null) throw new NullPointerException("event cannot be null");
		event.setCommand(getCommand(event));
	}
//	public boolean hasCommand(String name) {
//		for(gcCommand command : commands.values())
//			if(command.hasCommand(name))
//				return true;
//		return false;
//	}
	public pxnCommand getCommand(gcServerEventCommand event) {
		if(event == null) throw new NullPointerException("event cannot be null");
		return getCommand(event.getCommandStr());
	}
	public pxnCommand getCommand(String name) {
		if(name == null) throw new NullPointerException("name cannot be null");
		for(Entry<String, pxnCommand> entry : commands.entrySet()) {
			pxnCommand command = entry.getValue();
			if(command.equalsCommand(name))
				return entry.getValue();
		}
		return null;
	}


}
