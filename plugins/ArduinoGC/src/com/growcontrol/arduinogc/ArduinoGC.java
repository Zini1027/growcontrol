package com.growcontrol.arduinogc;

import com.growcontrol.arduinogc.interfaces.interfaceArduino;
import com.growcontrol.arduinogc.listeners.CommandsListener;
import com.growcontrol.arduinogc.listeners.OutputListener;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;

public class ArduinoGC extends gcServerPlugin {
	private static final String PLUGIN_NAME = "ArduinoGC";

	public static gcLogger log = getLogger(PLUGIN_NAME);

	public interfaceArduino arduino = null;


	@Override
	public void onEnable() {
		// register plugin name
		registerPlugin(PLUGIN_NAME);
		// register commands
		registerCommand("arduinogc")
			.addAlias("arduino");
		// register listeners
		registerListenerCommand(new CommandsListener());
		registerListenerOutput(new OutputListener());
		// load configs
//arduino = new ArduinoUSB(this, "/dev/ttyUSB0");
	}


	@Override
	public void onDisable() {
		if(arduino != null) {
			arduino.Close();
			arduino = null;
		}
		log.info("ArduinoGC disabled!");
	}


}
