package com.growcontrol.gcServer.serverPlugin;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListener;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListener.ListenerType;

public abstract class gcServerPlugin {

	protected gcServerPluginHolder pluginHolder;


	// load/unload plugin
	public abstract void onEnable();
	public abstract void onDisable();
	public boolean isEnabled() {
		return pluginHolder.isEnabled();
	}
	public void setHolder(gcServerPluginHolder pluginHolder) {
		if(pluginHolder == null) throw new NullPointerException();
		this.pluginHolder = pluginHolder;
	}
	protected void registerPlugin(String pluginName) {
		if(pluginName == null) throw new NullPointerException();
		pluginHolder.pluginName = pluginName;
		gcServer.log.info("Starting server plugin: "+pluginName);
	}


	// plugin logger
	public gcLogger getLogger() {
		return gcLogger.getLogger(pluginHolder.pluginName);
	}
	public static gcLogger getLogger(String pluginName) {
		return gcLogger.getLogger(pluginName);
	}


	// register listeners
	protected void registerListener(ListenerType type, gcServerListener listener) {
		gcServer.pluginManager.registerListener(pluginHolder.className, type, listener);
	}


}
