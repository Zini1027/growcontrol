package com.growcontrol.gctimer;

import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;
import com.growcontrol.gctimer.listeners.TickListener;

public class gcTimer extends gcServerPlugin {
	private static final String PLUGIN_NAME = "gcTimer";

	public static gcLogger log = getLogger(PLUGIN_NAME);


	@Override
	public void onEnable() {
		// register plugin name
		registerPlugin(PLUGIN_NAME);
		// register commands
		registerCommand("timer");
		// register listeners
		registerListenerCommand(new CommandsListener());
		registerListenerTick(new TickListener());
//		registerListenerOutput(new OutputListener());
	}


	@Override
	public void onDisable() {
		log.info("gcTimer disabled!");
	}


}
