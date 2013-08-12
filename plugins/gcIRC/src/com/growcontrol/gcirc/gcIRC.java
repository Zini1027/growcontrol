package com.growcontrol.gcirc;

import com.growcontrol.gcCommon.pxnPlugin.pxnPlugin;

import f00f.net.irc.martyr.IRCConnection;
import f00f.net.irc.martyr.clientstate.ClientState;
import f00f.net.irc.martyr.services.AutoJoin;
import f00f.net.irc.martyr.services.AutoReconnect;
import f00f.net.irc.martyr.services.AutoRegister;
import f00f.net.irc.martyr.services.AutoResponder;


public class gcIRC extends pxnPlugin {

	private static gcIRC instance = null;

	private AutoReconnect autoConnect = null;


	public static gcIRC get() {
		return instance;
	}
	public gcIRC() {
		super();
		if(instance == null)
			instance = this;
	}


	// load/unload plugin
	@Override
	public void onEnable() {
		Config.get();
		if(!Config.isLoaded()) {
			getLogger().severe("Failed to load bot.yml");
			return;
		}
	String key = null;
		this.getLogger().info("Connecting to: "+Config.Host());

		ClientState state = new ClientState();
		IRCConnection irc = new IRCConnection(state);
		// Provides "reflex" functions, such as responding to pings and asking for a channel's mode upon joining.
		new AutoResponder(irc);
		// Registers on the network, including different nick attempts.
		new AutoRegister(irc, "gcBot", "gcBot", "Saint");
		// Maintains a connection to a server.
		autoConnect = new AutoReconnect(irc);
		// Maintains a presence in a channel.
		for(String channel : Config.Channels())
			new AutoJoin(irc, channel, key);
		// connect
		autoConnect.go(Config.Host(), Config.Port());
	}
	@Override
	public void onDisable() {
	}


}
