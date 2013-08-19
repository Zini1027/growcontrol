package com.growcontrol.gcirc;

import com.growcontrol.gcServer.serverPlugin.gcServerPlugin;


public class gcIRC extends gcServerPlugin {

//	private AutoReconnect autoConnect = null;

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
//		String key = null;
//		this.getLogger().info("Connecting to: "+Config.Host());
//		ClientState state = new ClientState();
//		IRCConnection irc = new IRCConnection(state);
//		// Provides "reflex" functions, such as responding to pings and asking for a channel's mode upon joining.
//		new AutoResponder(irc);
//		// Registers on the network, including different nick attempts.
//		new AutoRegister(irc, "gcBot", "gcBot", "gcBot");
//		// Maintains a connection to a server.
//		autoConnect = new AutoReconnect(irc);
//		// Maintains a presence in a channel.
//		for(String channel : Config.Channels())
//			new AutoJoin(irc, channel, key);
//		// connect
//		autoConnect.go(Config.Host(), Config.Port());
	}
	@Override
	public void onDisable() {
	}


}
