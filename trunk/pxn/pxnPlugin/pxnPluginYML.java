package com.poixson.pxnPlugin;

import java.io.File;
import java.io.IOException;

import com.poixson.pxnConfig.pxnConfig;
import com.poixson.pxnLogger.pxnLogger;


public class pxnPluginYML {

	public pxnConfig config = null;;


	public pxnPluginYML(File file, String fileName) {
		if(file     == null) throw new NullPointerException("file cannot be null!");
		if(fileName == null) throw new NullPointerException("fileName cannot be null!");
		try {
			config = pxnConfig.loadJarResource(file, fileName);
		} catch (IOException e) {
			pxnLogger.log().exception(e);
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
	// main class name
	public String getMainClassValue() {
		return getMainClassValue("main");
	}
	public String getMainClassValue(String mainClassName) {
		if(config == null) return null;
		return config.getString(mainClassName);
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
