package com.growcontrol.gcCommon.pxnPlugin;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public abstract class pxnPlugin {

	private pxnPluginManager pluginManager = null;
	private pxnPluginYML yml = null;

	// load/unload plugin
	public abstract String getPluginName();
	public abstract void onEnable();
	public abstract void onDisable();
	public abstract void registerCommandsHolder(pxnCommandsHolder listener);


	// plugin is enabled
	private boolean enabled = false;
	protected void doEnable() {
		if(enabled) return;
		getLogger().info("Starting plugin..");
		onEnable();
		enabled = true;
	}
	protected void doDisable() {
		if(!enabled) return;
		getLogger().info("Stopping plugin..");
		enabled = false;
		onDisable();
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
	// plugin.yml
	public pxnPluginYML getPluginYML() {
		if(yml == null) throw new NullPointerException("yml not set!");
		return yml;
	}
	public void setPluginYML(pxnPluginYML yml) {
		if(yml == null) throw new NullPointerException("yml cannot be null!");
		this.yml = yml;
	}


	// plugin logger
	private pxnLogger log = null;
	public pxnLogger getLogger() {
		if(log == null)
			log = pxnLogger.get("plugin-"+getPluginName());
		return log;
	}


}
