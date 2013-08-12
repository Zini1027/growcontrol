package com.growcontrol.gcCommon.pxnPlugin;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandListenerGroup;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public abstract class pxnPlugin {

	private pxnPluginManager pluginManager = null;
	private pxnPluginYML yml = null;


	// load/unload plugin
	public abstract void onEnable();
	public abstract void onDisable();


	// plugin name
	public String getName() {
		return getPluginYML().getPluginName();
	}
	// version
	public String getVersion() {
		return getPluginYML().getPluginVersion();
	}


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
		// set only once
		if(this.pluginManager != null) return;
		this.pluginManager = pluginManager;
	}
	// plugin.yml
	public pxnPluginYML getPluginYML() {
		if(yml == null) throw new NullPointerException("yml not set!");
		return yml;
	}
	public void setPluginYML(pxnPluginYML yml) {
		if(yml == null) throw new NullPointerException("yml cannot be null!");
		// set only once
		if(this.yml != null) return;
		this.yml = yml;
	}


	// plugin logger
	private pxnLogger log = null;
	public pxnLogger getLogger() {
		if(log == null)
			log = pxnLog.get(getName());
		return log;
	}


	// commands holder
	public void register(pxnCommandsHolder listener) {
		pxnCommandListenerGroup.get().register(listener);
	}
	// trigger command
	public boolean triggerCommand(String line) {
		return pxnCommandListenerGroup.get().triggerCommandEvent(line);
	}


}
