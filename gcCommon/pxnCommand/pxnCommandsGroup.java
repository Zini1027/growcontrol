package com.growcontrol.gcCommon.pxnCommand;

import java.util.HashMap;

import com.growcontrol.gcCommon.pxnEvent.pxnEvent.EventPriority;
import com.growcontrol.gcCommon.pxnListener.pxnListener;
import com.growcontrol.gcCommon.pxnListener.pxnListenerGroup;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class pxnCommandsGroup extends pxnListenerGroup {

	private HashMap<String, pxnCommand> commands = new HashMap<String, pxnCommand>();


	// register command listener
	public void register(pxnCommand command) {
		if(command == null) throw new NullPointerException("command cannot be null!");
pxnLogger.getLogger().debug("Registered command: "+command.toString());
		// register listener
		this.register( (pxnListener) command);
		// register command
		synchronized(commands) {
			commands.put(command.toString(), command);
		}
	}
	// new command
	public pxnCommand addCommand(String commandStr) {
		if(commandStr == null)   throw new NullPointerException("commandStr cannot be null!");
		if(commandStr.isEmpty()) throw new NullPointerException("commandStr cannot be empty!");
		commandStr = commandStr.toLowerCase();
		// command exists
		if(commands.containsKey(commandStr))
			return commands.get(commandStr);
		// new command
		pxnCommand command = new pxnCommand(commandStr);
		this.register(command);
		return command;
	}
	protected pxnCommand add(String commandStr) {
		return this.addCommand(commandStr);
	}


	// priority
	public void setAllPriority(EventPriority priority) {
		if(priority == null) priority = EventPriority.NORMAL;
		for(pxnCommand command : this.commands.values())
			command.setPriority(priority);
	}


	// trigger command
	public boolean triggerEvent(pxnCommandEvent event, EventPriority onlyPriority) {
		if(event        == null) throw new NullPointerException("event cannot be null!");
		if(onlyPriority == null) throw new NullPointerException("onlyPriority cannot be null!");
		return false;
//		synchronized(listeners) {
//			// loop listeners
//			for(pxnListener listener : listeners)
//				if(listener.priorityEquals(onlyPriority))
//					if(listener.doEvent(event))
//						event.setHandled();
//			return event.isHandled();
//		}
	}
	// do event
	public boolean onCommand(pxnCommandEvent event) {
return false;
//TODO:
	}


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
