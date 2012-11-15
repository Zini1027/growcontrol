package com.growcontrol.arduinogc;

import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.serverPlugin.events.gcServerEvent;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerCommand;

public class CommandsListener implements gcServerPluginListenerCommand {


	@Override
	public boolean onCommand(gcCommand command, String[] args) {
ArduinoGC.log.severe("Command: "+command.toString());
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
		return false;
	}

	@Override
	public boolean fire(gcServerEvent event) {
		// TODO Auto-generated method stub
		ArduinoGC.log.severe("Commands listener fired!");
		return false;
	}


}
