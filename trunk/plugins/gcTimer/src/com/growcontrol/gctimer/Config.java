package com.growcontrol.gctimer;

import java.util.List;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnConfig.pxnConfigLoader;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gctimer.config.TimerDAO;


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
			return "plugins/"+gcTimer.get().getName()+"/";
		return configPath;
	}
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
