package com.growcontrol.gcServer.serverPlugin.listeners;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcServer.serverPlugin.commands.gcCommand;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;

public abstract class gcServerListenerCommand extends gcServerListener {

	protected HashMap<String, gcCommand> commands = new HashMap<String, gcCommand>();


	// do event
	public abstract boolean onCommand(gcServerEventCommand event);
	public boolean doEvent(gcServerEventCommand event) {
		if(event == null) throw new NullPointerException();
		setHasCommand(event);
//		event.hasCommand(hasCommand(event.getCommandStr()));
//System.out.println("HAS COMMAND: "+Boolean.toString(event.hasCommand()));
		return onCommand(event);
	}


	// new command
	public gcCommand add(String name) {
		return addCommand(name);
	}
	public gcCommand addCommand(String name) {
		if(name == null) throw new NullPointerException();
		if(commands.containsKey(name)) {
			return commands.get(name);
		} else {
			gcCommand command = new gcCommand(name);
			commands.put(name, command);
			return command;
		}
	}


	// find command/alias
	protected void setHasCommand(gcServerEventCommand event) {
		event.setCommand(getCommand(event));
	}
//	public boolean hasCommand(String name) {
//		for(gcCommand command : commands.values())
//			if(command.hasCommand(name))
//				return true;
//		return false;
//	}
	public gcCommand getCommand(gcServerEventCommand event) {
		return getCommand(event.getCommandStr());
	}
	public gcCommand getCommand(String name) {
		for(Entry<String, gcCommand> entry : commands.entrySet()) {
			gcCommand command = entry.getValue();
			if(command.hasCommand(name))
				return entry.getValue();
		}
		return null;
	}


}
