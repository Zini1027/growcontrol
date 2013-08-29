package com.growcontrol.example;

import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;


public class example extends gcServerPlugin {


	// load/unload plugin
	@Override
	public void onEnable() {
		// register listeners
		register(Commands.get());
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
