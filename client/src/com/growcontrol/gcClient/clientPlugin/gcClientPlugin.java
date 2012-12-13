package com.growcontrol.gcClient.clientPlugin;

import com.poixson.pxnPlugin.pxnPlugin;


public abstract class gcClientPlugin  extends pxnPlugin {


	// get plugin manager
	@Override
	public gcClientPluginManager getPluginManager() {
		return (gcClientPluginManager) this.pluginManager;
	}


	// client gui frames for plugins
	public gcPluginFrame newFrame(String title) {
		gcClientPluginManager pluginManager = getPluginManager();
		if(pluginManager == null) throw new NullPointerException("pluginManager hasn't been set!");
		return pluginManager.newFrame(title);
	}
	public void addFrame(gcPluginFrame frame) {
		gcClientPluginManager pluginManager = getPluginManager();
		if(pluginManager == null) throw new NullPointerException("pluginManager hasn't been set!");
		pluginManager.addFrame(frame);
	}


}
