package com.growcontrol.gcServer;

import java.io.File;
import java.util.List;

import com.growcontrol.gcServer.config.gcConfig;

public class ServerConfig {

	protected gcConfig config = null;
	protected String configsPath = "";

	public String version = null;
	public String logLevel = null;
	public long tickInterval = 1000;
	public int listenPort = 1142;


	// load config.yml
	public ServerConfig() {
		this(null);
	}
	public ServerConfig(String path) {
		if(path != null) configsPath = path;
		if(!configsPath.isEmpty())
			if(!configsPath.endsWith(File.separator))
				configsPath += File.separator;
		gcServer.log.debug("Loading config file: "+configsPath+"config.yml");
		if(configsPath.isEmpty())
			config = gcConfig.loadFile("config.yml");
		else
			config = gcConfig.loadFile(configsPath, "config.yml");
		if(config == null) return;
		version = config.getString("Version");
		logLevel = config.getString("Log Level");
		tickInterval = config.getLong("Tick Interval");
		listenPort = config.getInt("Listen Port");
		if(listenPort < 1) listenPort = 1142;
	}


	// zones (rooms)
	public List<String> getZones() {
		try {
			@SuppressWarnings("unchecked")
			List<String> zones = (List<String>) config.get("zones");
			return zones;
		} catch(Exception ignore) {
			return null;
		}
	}


}
