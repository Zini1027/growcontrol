package com.poixson.pxnPlugin;

import com.poixson.pxnLogger.pxnLogger;


public abstract class pxnPlugin {

	// plugin is enabled
	protected boolean enabled = false;

	// plugin manager
	protected pxnPluginManager pluginManager;


	// load/unload plugin
	public abstract String getPluginName();
	public abstract void onEnable();
	public abstract void onDisable();


	// plugin is enabled
	public boolean isEnabled() {
		return enabled;
	}


	// plugin manager
	public pxnPluginManager getPluginManager() {
		return pluginManager;
	}
	public void setPluginManager(pxnPluginManager pluginManager) {
		if(pluginManager == null) throw new NullPointerException("pluginManager cannot be null!");
		this.pluginManager = pluginManager;
	}


	// plugin logger
	public pxnLogger getLogger() {
		return getLogger(getPluginName());
	}
	public static pxnLogger getLogger(String pluginName) {
		return pxnLogger.getLogger(pluginName);
	}


}