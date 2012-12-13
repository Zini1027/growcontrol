package com.growcontrol.gctimer.client;

import com.growcontrol.gcClient.clientPlugin.gcClientPlugin;
import com.growcontrol.gctimer.client.frames.gcTimerFrame;


public class gcTimerClient extends gcClientPlugin {

	// plugin name
	private static final String PLUGIN_NAME = "ArduinoGC";
//	// logger
//	public static gcLogger log = getLogger(PLUGIN_NAME);


	@Override
	public String getPluginName() {
		return PLUGIN_NAME;
	}
	@Override
	public void onEnable() {
		getPluginManager().addFrame(new gcTimerFrame());
	}
	@Override
	public void onDisable() {
		//
	}


}
