package com.growcontrol.gcServer;

import java.util.Collection;

import com.poixson.pxnUtils;
import com.poixson.pxnConfig.pxnConfig;


public class ServerConfig {

	protected pxnConfig config = null;
	protected String configsPath = "";


	// load config.yml
	public ServerConfig() {
		this(null);
	}
	public ServerConfig(String path) {
		if(path != null) configsPath = path;
		try {
			config = pxnConfig.loadFile(configsPath, "config.yml");
		} catch (Exception e) {
			gcServer.getLogger().exception(e);
		}
	}


	// version
	public String getVersion() {
		if(config == null) return null;
		return config.getString("Version");
	}


	// log level
	public String getLogLevel() {
		if(config == null) return null;
		return config.getString("Log Level");
	}


	// tick interval
	public long getTickInterval() {
		if(config == null) return 1000;
		return pxnUtils.MinMax(
			config.getLong("Tick Interval"),
			1,
			60000);
	}


	// listen port
	public int getListenPort() {
		if(config == null) return 1142;
		return pxnUtils.MinMax(
			config.getInt("Listen Port"),
			1,
			65536);
	}


	// zones (rooms)
	public void getZones(Collection<String> zones) {
		if(config == null) return;
		if(zones  == null) throw new NullPointerException("zones list can't be null!");
		try {
			zones.addAll(config.getStringList("Zones"));
//			zones.addAll( pxnUtils.castList(String.class, config.get("Zones")) );
//			zones.addAll((Collection<? extends String>) config.get("Zones"));
		} catch(Exception ignore) {
ignore.printStackTrace();
			return;
		}
	}


}
