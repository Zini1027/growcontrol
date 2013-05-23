package com.gcCommon.pxnCommand;

import java.util.HashMap;

import com.gcCommon.pxnEvent.pxnEvent.EventPriority;
import com.gcCommon.pxnListener.pxnListener;
import com.gcCommon.pxnListener.pxnListenerGroup;
import com.gcCommon.pxnLogger.pxnLogger;


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
		commandStr = commandStr.toLowerCase();
		// command exists
		if(commands.containsKey(commandStr))
			return commands.get(commandStr);
		// new command
		pxnCommand command = new pxnCommand(commandStr);
		this.register(command);
		return command;
	}


	// trigger event
	public boolean triggerEvent(pxnCommandEvent event, EventPriority onlyPriority) {
		return false;
//		if(event        == null) throw new NullPointerException("event cannot be null!");
//		if(onlyPriority == null) throw new NullPointerException("onlyPriority cannot be null!");
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
//	public abstract boolean onCommand(gcServerEventCommand event);
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