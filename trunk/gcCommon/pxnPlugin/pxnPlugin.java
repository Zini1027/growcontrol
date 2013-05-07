package com.gcCommon.pxnPlugin;

import com.gcCommon.pxnLogger.pxnLogger;


public abstract class pxnPlugin {


	// load/unload plugin
	public abstract String getPluginName();
	public abstract void onEnable();
	public abstract void onDisable();


	// plugin is enabled
	private boolean enabled = false;
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}


	// plugin manager
	private pxnPluginManager pluginManager;
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
