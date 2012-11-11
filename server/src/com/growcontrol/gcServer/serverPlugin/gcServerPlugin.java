package com.growcontrol.gcServer.serverPlugin;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.commands.gcCommand;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerCommand;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerPluginListenerDevice;
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
		if(pluginHolder == null) throw new NullPointerException();
		this.pluginHolder = pluginHolder;
	}
	protected void registerPlugin(String pluginName) {
		if(pluginName == null) throw new NullPointerException();
		pluginHolder.pluginName = pluginName;
		gcServer.log.info("Starting server plugin: "+pluginName);
	}


	// register commands
	protected gcCommand registerCommand(String name) {
		if(name == null) throw new NullPointerException();
		return pluginHolder.commands.addCommand(name);
	}


	// register listeners
	protected void registerListenerCommand(gcServerPluginListenerCommand listener) {
		if(listener == null) throw new NullPointerException();
		gcServerPluginLoader.registerListenerCommand(pluginHolder.className, listener);
		getLogger().debug("Registered command listener");
	}
	protected void registerListenerTick(gcServerPluginListenerTick listener) {
		if(listener == null) throw new NullPointerException();
		gcServerPluginLoader.registerListenerTick(pluginHolder.className, listener);
		getLogger().debug("Registered tick listener");
	}
	protected void registerListenerOutput(gcServerPluginListenerOutput listener) {
		if(listener == null) throw new NullPointerException();
		gcServerPluginLoader.registerListenerOutput(pluginHolder.className, listener);
		getLogger().debug("Registered output listener");
	}
	protected void registerListenerInput(gcServerPluginListenerInput listener) {
		if(listener == null) throw new NullPointerException();
		gcServerPluginLoader.registerListenerInput(pluginHolder.className, listener);
		getLogger().debug("Registered input listener");
	}
	protected void registerListenerDevice(gcServerPluginListenerDevice listener) {
		if(listener == null) throw new NullPointerException();
		gcServerPluginLoader.registerListenerDevice(pluginHolder.className, listener);
		getLogger().debug("Registered device listener");
	}


	// plugin logger
	public gcLogger getLogger() {
		return gcLogger.getLogger(pluginHolder.pluginName);
	}
	public static gcLogger getLogger(String pluginName) {
		return gcLogger.getLogger(pluginName);
	}


}
