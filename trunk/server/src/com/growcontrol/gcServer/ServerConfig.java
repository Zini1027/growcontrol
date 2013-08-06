package com.growcontrol.gcServer;

import java.util.Collection;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public class ServerConfig {

	// path to config files
	protected String configsPath = null;
	// config loader
	protected pxnConfig config = null;
	// instance of this
	protected static ServerConfig serverConfig = null;


	public static ServerConfig get() {
		if(serverConfig == null)
			serverConfig = new ServerConfig(null);
		return serverConfig;
	}
	protected static ServerConfig get(String dirPath) {
		if(serverConfig == null)
			serverConfig = new ServerConfig(dirPath);
		return serverConfig;
	}
	public static boolean isLoaded() {
		if(serverConfig == null)
			return false;
		if(serverConfig.config == null)
			return false;
		return serverConfig.config.isLoaded();
	}


	// load config.yml
	protected ServerConfig(String dirPath) {
		if(dirPath != null)
			configsPath = dirPath;
		try {
			config = pxnConfig.loadFile(configsPath, "config.yml");
		} catch (Exception e) {
			pxnLog.get().exception(e);
			config = null;
		}
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	// config has loaded
	public boolean hasLoaded() {
		return (config != null);
	}


	// version
	public String Version() {
		if(config == null) return null;
		return config.getString("Version");
	}


	// log level
	public String LogLevel() {
		if(config == null) return null;
		return config.getString("Log Level");
	}


	// tick interval
	public long TickInterval() {
		if(config == null) return 1000;
		return pxnUtils.MinMax(
			config.getLong("Tick Interval"),
			1,
			60000);
	}


	// listen port
	public int ListenPort() {
		if(config == null) return 1142;
		return pxnUtils.MinMax(
			config.getInt("Listen Port"),
			1,
			65536);
	}


	// zones (rooms)
	public void PopulateZones(Collection<String> zones) {
		if(config == null) return;
		if(zones  == null) throw new NullPointerException("zones list can't be null!");
		try {
			zones.addAll(config.getStringList("Zones"));
//			zones.addAll( pxnUtils.castList(String.class, config.get("Zones")) );
//			zones.addAll((Collection<? extends String>) config.get("Zones"));
		} catch(Exception ignore) {
pxnLog.get().exception(ignore);
			return;
		}
	}


}
