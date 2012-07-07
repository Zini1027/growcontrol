package com.growcontrol.arduinogc.listeners;

import com.growcontrol.arduinogc.ArduinoGC;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerOutput;

public class OutputListener implements gcServerPluginListenerOutput {


	@Override
	public boolean onOutput(String[] args) {
ArduinoGC.log.severe("OUTPUT!!!!!!! "+Integer.toString(args.length));
		return true;
	}


}
