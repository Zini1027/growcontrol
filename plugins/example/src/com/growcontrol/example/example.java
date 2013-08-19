package com.growcontrol.example;

import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;


public class example extends gcServerPlugin {

	// commands listener
	private Commands commands = new Commands();


	// load/unload plugin
	@Override
	public void onEnable() {
		// register listeners
		if(commands == null)
			commands = new Commands();
		register(commands);
		// load configs
		Config.get("plugins/"+getName()+"/");
		if(!Config.isLoaded()) {
			getLogger().severe("Failed to load "+Config.CONFIG_FILE);
			return;
		}
	}
	@Override
	public void onDisable() {
	}


}
