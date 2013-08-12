package com.growcontrol.gcirc;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnConfig.pxnConfigLoader;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public final class Config {
	private Config() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private static final String CONFIG_FILE = "timers.yml";
	private static String configPath = null;

	// config dao
	protected static pxnConfig config = null;


	public static synchronized pxnConfig get() {
		if(config == null) {
			String fileStr = pxnUtils.BuildFilePath(getPath(), CONFIG_FILE);
			pxnLog.get().debug("Loading config file: "+fileStr);
			config = pxnConfigLoader.LoadConfig(fileStr);
		}
		return config;
	}
	public static boolean isLoaded() {
		return (config != null);
	}


	// configs path
	public static String getPath() {
		if(configPath == null || configPath.isEmpty())
			return "plugins/"+gcIRC.get().getName()+"/";
		return configPath;
	}
	public static void setPath(String path) {
		configPath = path;
	}


	// host
	public static String Host() {
		pxnConfig config = get();
		if(config == null) return null;
		return config.getString("Host");
	}
	// port
	public static int Port() {
		pxnConfig config = get();
		if(config == null) return 6667;
		return config.getInt("Port");
	}
	// channels
	public static String[] Channels() {
		pxnConfig config = get();
		if(config == null) return null;
		return config.getStringList("Channels").toArray(new String[0]);
	}
	// nick
	public static String Nick() {
		pxnConfig config = get();
		if(config == null) return null;
		return config.getString("Nick");
	}


}
