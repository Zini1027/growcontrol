package com.growcontrol.gctimer.listeners;

import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerCommand;
import com.growcontrol.gctimer.gcTimer;

public class CommandsListener implements gcServerPluginListenerCommand {


	@Override
	public boolean onCommand(gcCommand command, String[] args) {
		gcTimer.log.severe("PLUGIN COMMAND!!!"+command.toString());
		return true;
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
