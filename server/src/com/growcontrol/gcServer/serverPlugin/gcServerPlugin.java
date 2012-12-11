package com.growcontrol.gcServer.serverPlugin;

import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.listeners.gcServerListenerCommand;
import com.poixson.pxnPlugin.pxnPlugin;


public abstract class gcServerPlugin extends pxnPlugin {


	// get logger
	public static gcLogger getLogger(String pluginName) {
		return gcLogger.getLogger(pluginName);
	}


	// register listeners
	public void registerCommandListener(gcServerListenerCommand listener) {
		if(listener == null) throw new NullPointerException("listener can't be null!");
		if(pluginManager == null) throw new NullPointerException("pluginManager hasn't been set!");
		((gcServerPluginManager) pluginManager).registerCommandListener(listener);
	}


}
