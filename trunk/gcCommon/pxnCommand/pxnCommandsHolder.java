package com.growcontrol.gcCommon.pxnCommand;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcCommon.pxnEvent.pxnEvent;
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


//	// register command listener
//	public void register(pxnCommand command) {
//		if(command == null) throw new NullPointerException("command cannot be null!");
//pxnLogger.getLogger().debug("Registered command: "+command.toString());
//		// register listener
//		this.register( (pxnListener) command);
//		// register command
//		synchronized(commands) {
//			commands.put(command.toString(), command);
//		}
//	}
//	// new command
//	public pxnCommand addCommand(String commandStr) {
//		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null!");
//		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty!");
//		commandStr = commandStr.toLowerCase();
//		// command exists
//		if(commands.containsKey(commandStr))
//			return commands.get(commandStr);
//		// new command
//		pxnCommand command = new pxnCommand(commandStr);
//		this.register(command);
//		return command;
//	}
//	protected pxnCommand add(String commandStr) {
//		return this.addCommand(commandStr);
//	}


//	// priority
//	public void setAllPriority(EventPriority priority) {
//		if(priority == null) priority = EventPriority.NORMAL;
//		for(pxnCommand command : this.commands.values())
//			command.setPriority(priority);
//	}


//	// trigger command
//	public boolean triggerEvent(pxnCommandEvent event, EventPriority onlyPriority) {
//		if(event        == null) throw new NullPointerException("event cannot be null!");
//		if(onlyPriority == null) throw new NullPointerException("onlyPriority cannot be null!");
//		return false;
////		synchronized(listeners) {
////			// loop listeners
////			for(pxnListener listener : listeners)
////				if(listener.priorityEquals(onlyPriority))
////					if(listener.doEvent(event))
////						event.setHandled();
////			return event.isHandled();
////		}
//	}
//	// do event
//	public boolean onCommand(pxnCommandEvent event) {
//return false;
//TODO:
//	}


//	public boolean doEvent(gcServerEventCommand event) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//System.out.println("ONCOMMAND "+event.toString());
//		setHasCommand(event);
//		event.hasCommand(hasCommand(event.getCommandStr()));
//		return onCommand(event);
//	}




//	// find command/alias
//	protected void setHasCommand(gcServerEventCommand event) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//		event.setCommand(getCommand(event));
//	}
//	public boolean hasCommand(String name) {
//		for(gcCommand command : commands.values())
//			if(command.hasCommand(name))
//				return true;
//		return false;
//	}
//	public pxnCommand getCommand(gcServerEventCommand event) {
//		if(event == null) throw new NullPointerException("event cannot be null");
//		return getCommand(event.getLine().getFirst());
//	}
//	public pxnCommand getCommand(String name) {
//		if(name == null) throw new NullPointerException("name cannot be null");
//		for(Entry<String, pxnCommand> entry : commands.entrySet()) {
//			pxnCommand command = entry.getValue();
//			if(command.equalsCommand(name))
//				return entry.getValue();
//		}
//		return null;
//	}



}
