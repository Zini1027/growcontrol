package com.growcontrol.arduinogc;

import com.growcontrol.gcServer.serverPlugin.commands.gcCommand;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent.EventPriority;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEventCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListenerCommand;

public class CommandsListener extends gcServerListenerCommand {


	public CommandsListener() {
		setPriority(EventPriority.NORMAL);
		// register commands
		add("arduinogc")
			.addAlias("arduino")
			.setUsage("");
	}


	@Override
	public boolean onCommand(gcServerEventCommand event) {
		if(event.isHandled())   return false;
		if(!event.hasCommand()) return false;
		gcCommand command = event.getCommand();
ArduinoGC.log.severe("Command: "+command.toString());
		return true;
	}


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
//		return false;


}
