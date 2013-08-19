package com.growcontrol.gcirc;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandEvent;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;


public class Commands extends pxnCommandsHolder {


	@Override
	protected void initCommands() {
//		setPriority(EventPriority.NORMAL);
		// register commands
		addCommand("bot")
			.addAlias("irc")
			.setUsage("");
	}


	@Override
	public boolean onCommand(pxnCommandEvent event) {
return false;
//		if(event.isHandled())   return false;
//		if(!event.hasCommand()) return false;
//		pxnCommand command = event.getCommand();
//ArduinoGC.log.severe("ARDUINO Command: "+command.toString());
//		return true;
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
