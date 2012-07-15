package com.growcontrol.gcServer;

import java.util.List;

import com.growcontrol.gcServer.config.gcConfig;

public class ServerConfig {

	protected gcConfig config = null;

	public String version = null;
	public String logLevel = null;
	public long tickInterval = 1000;


	public ServerConfig() {
		config = gcConfig.loadFile("config.yml");
		if(config == null) return;
		version = config.getString("version");
		logLevel = config.getString("log level");
		tickInterval = config.getLong("tick interval");
	}


	public List<String> getRooms() {
		try {
			@SuppressWarnings("unchecked")
			List<String> rooms = (List<String>) config.get("rooms");
			return rooms;
		} catch(Exception ignore) {
			return null;
		}
	}


}
