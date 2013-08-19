package com.growcontrol.example;

import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnConfig.pxnConfigLoader;


public final class Config {
	private Config() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static final String CONFIG_FILE = "timers.yml";
	private static volatile String configPath = null;

	// config dao
	protected static volatile pxnConfig config = null;
	protected static final Object lock = new Object();


	public static pxnConfig get(String path) {
		setPath(path);
		return get();
	}
	public static pxnConfig get() {
		if(config == null) {
			synchronized(lock) {
				if(config == null)
					config = pxnConfigLoader.Load(configPath, CONFIG_FILE);
			}
		}
		return config;
	}
	public static boolean isLoaded() {
		return (config != null);
	}


	// configs path
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
