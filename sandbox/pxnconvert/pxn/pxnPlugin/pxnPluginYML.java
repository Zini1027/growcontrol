package com.poixson.pxnPlugin;

import java.io.File;
import java.io.IOException;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.config.gcConfig;

public class pxnPluginYML {

	public gcConfig config = null;;


	public pxnPluginYML(File file, String fileName) {
		if(file     == null) throw new NullPointerException("file cannot be null");
		if(fileName == null) throw new NullPointerException("fileName cannot be null");
		try {
			config = gcConfig.loadJarResource(file, fileName);
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
	}


	// plugin name
	public String getPluginName() {
		if(config == null) return null;
		return config.getString("Plugin Name");
	}
	// plugin version
	public String getPluginVersion() {
		if(config == null) return null;
		return config.getString("Plugin Version");
	}
	// server main class
	public String getServerMain() {
		if(config == null) return null;
		return config.getString("Server Main");
	}
	// client main class
	public String getClientMain() {
		if(config == null) return null;
		return config.getString("Client Main");
	}
	// author
	public String getAuthor() {
		if(config == null) return null;
		return config.getString("Author");
	}
	// website
	public String getWebsite() {
		if(config == null) return null;
		return config.getString("Website");
	}


}
