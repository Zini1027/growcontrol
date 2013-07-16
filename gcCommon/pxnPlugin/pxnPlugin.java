package com.growcontrol.gcCommon.pxnPlugin;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public abstract class pxnPlugin {

	private pxnPluginManager pluginManager;

	// load/unload plugin
	public abstract String getPluginName();
	public abstract void onEnable();
	public abstract void onDisable();
	public abstract void registerCommandsHolder(pxnCommandsHolder listener);


	// plugin is enabled
	private boolean enabled = false;
	protected void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}


	// plugin manager
	public pxnPluginManager getPluginManager() {
		if(pluginManager == null) throw new NullPointerException("pluginManager not set!");
		return pluginManager;
	}
	public void setPluginManager(pxnPluginManager pluginManager) {
		if(pluginManager == null) throw new NullPointerException("pluginManager cannot be null!");
		this.pluginManager = pluginManager;
	}


	// plugin logger
	public pxnLogger getLogger() {
		return pxnLogger.get("plugin-"+getPluginName());
	}


}
