package com.growcontrol.gctimer.listeners;

import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListenerCommand;
import com.growcontrol.gctimer.gcTimer;
import com.poixson.pxnCommand.pxnCommand;
import com.poixson.pxnEvent.pxnEvent.EventPriority;


public class CommandsListener extends gcServerListenerCommand {


	public CommandsListener() {
		setPriority(EventPriority.NORMAL);
		// register commands
		add("timer")
			.setUsage("");
	}


	@Override
	public boolean onCommand(gcServerEventCommand event) {
		if(event.isHandled())   return false;
		if(!event.hasCommand()) return false;
		pxnCommand command = event.getCommand();
gcTimer.log.severe("gcTimer Command: "+command.toString());
		return true;
//		pxnCommand command = event.getCommand();
//		String[] args = event.getArgs();
//		gcTimer.log.severe("PLUGIN COMMAND!!! "+command.toString());
//		return true;
//		// set output
//		if(command.equalsIgnoreCase("set")) {
//			if(numArgs < 3) return false;
//			if(!args[0].equalsIgnoreCase("ArduinoGC")) return false;
//			// get pin number
//			int pinNum = -1;
//			try {
//				pinNum = Integer.valueOf(args[1]);
//			} catch(Exception ignore) {}
//			if(pinNum < 0) return false;
//			// get pin state
//			int pinState = -1;
//			try {
//				pinState = Integer.valueOf(args[2]);
//			} catch(Exception ignore) {}
//			if(pinState < 0) return false;
//			// send to plugin
//			sendPinState(pinNum, pinState);
//			return true;
//		}
//		if(command.equalsIgnoreCase("get")) {
//			return true;
//		}
//		if(command.equalsIgnoreCase("watch")) {
//			return true;
//		}
	}


}
