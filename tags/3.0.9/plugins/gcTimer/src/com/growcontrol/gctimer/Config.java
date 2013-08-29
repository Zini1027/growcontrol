package com.growcontrol.gctimer;

import java.util.List;

import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnConfig.pxnConfigLoader;
import com.growcontrol.gctimer.config.TimerDAO;


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


	// version
	public static String Version() {
		pxnConfig config = get();
		if(config == null) return null;
		return config.getString("Version");
	}
	// timer
	public static List<TimerDAO> Timers() {
		if(config == null) return null;
		return TimerDAO.get(config.getConfigList("Timers"));
	}


}
