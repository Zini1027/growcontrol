package com.growcontrol.gcServer.serverPlugin;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerInput;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerOutput;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerTick;

public abstract class gcServerPlugin {

	protected gcServerPluginHolder pluginHolder;


	// load/unload plugin
	public abstract void onEnable();
	public abstract void onDisable();
	public boolean isEnabled() {
		return pluginHolder.isEnabled();
	}
	public void setHolder(gcServerPluginHolder pluginHolder) {
		this.pluginHolder = pluginHolder;
	}
	protected void registerPlugin(String pluginName) {
		pluginHolder.pluginName = pluginName;
		gcServer.log.info("Starting server plugin: "+pluginName);
	}


	// register commands
	protected gcCommand registerCommand(String name) {
		return pluginHolder.commands.addCommand(name);
	}


	// register listeners
	protected void registerListenerCommand(gcServerPluginListenerCommand listener) {
		gcServerPluginLoader.registerListenerCommand(pluginHolder.className, listener);
	}
	protected void registerListenerTick(gcServerPluginListenerTick listener) {
		gcServerPluginLoader.registerListenerTick(pluginHolder.className, listener);
	}
	protected void registerListenerOutput(gcServerPluginListenerOutput listener) {
		gcServerPluginLoader.registerListenerOutput(pluginHolder.className, listener);
	}
	protected void registerListenerInput(gcServerPluginListenerInput listener) {
		gcServerPluginLoader.registerListenerInput(pluginHolder.className, listener);
	}


	// plugin logger
	public gcLogger getLogger() {
		return gcLogger.getLogger(pluginHolder.pluginName);
	}
	public static gcLogger getLogger(String pluginName) {
		return gcLogger.getLogger(pluginName);
	}


}
