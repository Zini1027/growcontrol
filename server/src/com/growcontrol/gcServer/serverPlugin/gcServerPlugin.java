package com.growcontrol.gcServer.serverPlugin;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;
import com.growcontrol.gcCommon.pxnPlugin.pxnPlugin;


public abstract class gcServerPlugin extends pxnPlugin {

//	private pxnCommandsHolder commandsHolder = null;


	// get plugin manager
	@Override
	public gcServerPluginManager getPluginManager() {
		return (gcServerPluginManager) this.getPluginManager();
	}


	// get logger
	public static gcLogger getLogger(String pluginName) {
		return gcLogger.getLogger(pluginName);
	}


	// register listeners
	@Override
	public void registerCommandsHolder(pxnCommandsHolder listener) {
		if(listener == null) throw new NullPointerException("listener can't be null!");
		gcServerPluginManager pluginManager = getPluginManager();
		if(pluginManager == null) throw new NullPointerException("pluginManager hasn't been set!");
		gcServer.getListeners().registerCommandListener(listener);
	}


}
