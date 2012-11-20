package com.growcontrol.gcServer;

import java.util.List;

import com.growcontrol.gcServer.serverPlugin.config.gcConfig;

public class ServerConfig {

	protected gcConfig config = null;
	protected String configsPath = "";


	// load config.yml
	public ServerConfig() {
		this(null);
	}
	public ServerConfig(String path) {
		if(path != null) configsPath = path;
		try {
			config = gcConfig.loadFile(configsPath, "config.yml");
		} catch (Exception e) {
			gcServer.log.exception(e);
		}
	}


	// version
	public String getVersion() {
		if(config == null)
			return null;
		return config.getString("Version");
	}


	// log level
	public String getLogLevel() {
		if(config == null)
			return null;
		return config.getString("Log Level");
	}


	// tick interval
	public long getTickInterval() {
		if(config == null)
			return 1000;
		return pxnUtils.MinMax(
			config.getLong("Tick Interval"),
			1,
			60000);
	}


	// listen port
	public int getListenPort() {
		if(config == null)
			return 1142;
		return pxnUtils.MinMax(
			config.getInt("Listen Port"),
			1,
			65536);
	}


	// zones (rooms)
	public List<String> getZones() {
		if(config == null)
			return null;
		try {
			@SuppressWarnings("unchecked")
			List<String> zones = (List<String>) config.get("zones");
			return zones;
		} catch(Exception ignore) {
			return null;
		}
	}


}
