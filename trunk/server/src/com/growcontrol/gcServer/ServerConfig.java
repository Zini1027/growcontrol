package com.growcontrol.gcServer;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnConfig.pxnConfigLoader;
import com.growcontrol.gcCommon.pxnLogger.pxnLevel;


public final class ServerConfig {
	private ServerConfig() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private static final String CONFIG_FILE = "config.yml";
	private static volatile String configPath = null;

	// config dao
	protected static volatile pxnConfig config = null;
	protected static final Object lock = new Object();


	public static pxnConfig get() {
		if(config == null) {
			synchronized(lock) {
				if(config == null)
					config = pxnConfigLoader.Load(getPath(), CONFIG_FILE);
			}
		}
		return config;
	}
	public static boolean isLoaded() {
		return (config != null);
	}


	// configs path
	public static String getPath() {
		if(configPath == null || configPath.isEmpty())
			return "./";
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
	// log level
	public static pxnLevel LogLevel() {
		pxnConfig config = get();
		if(config == null) return null;
		String str = config.getString("Log Level");
		if(str == null || str.isEmpty()) return null;
		return pxnLevel.parse(str);
	}
	// tick interval
	public static long TickInterval() {
		long def = 1000;
		pxnConfig config = get();
		if(config == null) return def;
		Long l = config.getLong("Tick Interval");
		if(l == null) return def;
		return pxnUtils.MinMax(l.longValue(), 1, 60000);
	}
	// listen port
	public static int ListenPort() {
		int def = 1142;
		pxnConfig config = get();
		if(config == null) return def;
		Integer i = config.getInt("Listen Port");
		if(i == null) return def;
		return pxnUtils.MinMax(i.intValue(), 1, 65536);
	}
//	// max logic threads
//	public static int LogicThreads() {
//		if(config == null) return 1;
//		return pxnUtils.MinMax(
//			config.getInt("Logic Threads"),
//			1,
//			100);
//	}
//
//
//	// zones (rooms)
//	public static void PopulateZones(Collection<String> zones) {
//		if(config == null) return;
//		if(zones  == null) throw new NullPointerException("zones list can't be null!");
//		try {
//			zones.addAll(config.getStringList("Zones"));
////			zones.addAll( pxnUtils.castList(String.class, config.get("Zones")) );
////			zones.addAll((Collection<? extends String>) config.get("Zones"));
//		} catch(Exception ignore) {
//pxnLog.get().debug(ignore);
//			return;
//		}
//	}


}
