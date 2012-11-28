package com.poixson.pxnPlugin;

import com.poixson.pxnLogger.pxnLogger;


public abstract class pxnPlugin {

	// plugin manager
	protected pxnPluginManager pluginManager;
	protected boolean enabled = false;


	// load/unload plugin
	public abstract void onEnable();
	public abstract void onDisable();
	public abstract String getPluginName();


	public boolean isEnabled() {
		return enabled;
	}


	public void setManager(pxnPluginManager pluginManager) {
		if(pluginManager == null) throw new NullPointerException("pluginManager cannot be null");
		this.pluginManager = pluginManager;
	}
//gcServer.log.info("Starting server plugin: "+pluginName);


	// plugin logger
	public pxnLogger getLogger() {
		return getLogger(getPluginName());
	}
	public static pxnLogger getLogger(String pluginName) {
		return pxnLogger.getLogger(pluginName);
	}


//	// register listeners
//	protected void registerListener(ListenerType type, gcServerListener listener) {
//		gcServer.pluginManager.registerListener(pluginHolder.className, type, listener);
//	}









//public class gcServerPluginHolder2 {
//
//	// plugin instance
//	public final gcServerPlugin plugin;
//	public boolean enabled = false;
//
//	// plugin name
//	public static final String DEFAULT_PLUGIN_NAME = "Untitled gcPlugin";
//	public String pluginName = DEFAULT_PLUGIN_NAME;
//	// plugin class path
//	public String className = null;
//	// plugin data folder
////	private File dataFolder = null;
//
//
//	// new plugin instance
//	public gcServerPluginHolder(String className, Class<?> pluginClass)
//	throws InstantiationException, IllegalAccessException {
//		if(className   == null) throw new NullPointerException("className cannot be null");
//		if(pluginClass == null) throw new NullPointerException("pluginClass cannot be null");
//		this.className = className;
//		plugin = (gcServerPlugin) pluginClass.newInstance();
//		plugin.setHolder(this);
//	}
//
//
//	public void doEnable() {
//		try {
//			plugin.onEnable();
//			enabled = true;
//		} catch(Exception e) {
//			plugin.getLogger().exception(e);
//		}
//	}
//	public void doDisable() {
//		enabled = false;
//		try {
//			plugin.onDisable();
//		} catch(Exception e) {
//			plugin.getLogger().exception(e);
//		}
//	}
//	public boolean isEnabled() {
//		return enabled;
//	}
//}







}
