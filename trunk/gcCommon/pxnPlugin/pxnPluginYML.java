package com.growcontrol.gcCommon.pxnPlugin;

import java.io.File;
import java.io.IOException;

import com.growcontrol.gcCommon.pxnConfig.pxnConfig;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public class pxnPluginYML {

	public pxnConfig config = null;


	public pxnPluginYML(File file, String fileName) {
		if(file     == null) throw new NullPointerException("file cannot be null!");
		if(fileName == null) throw new NullPointerException("fileName cannot be null!");
		try {
			config = pxnConfig.loadJarResource(file, fileName);
		} catch (IOException e) {
			pxnLog.get().exception(e);
			config = null;
		}
	}


	// config has loaded
	public boolean hasLoaded() {
		return (config != null);
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
	public String getMainClass(String fieldName) {
		if(config == null) return null;
		if(fieldName == null || fieldName.isEmpty())
			fieldName = "Main Class";
		// get main class from plugin.yml
		String value = config.getString(fieldName);
		if(value == null || value.isEmpty()) return null;
		// trim .class from end
		if(value.endsWith(".class"))
			value = value.substring(0, value.length()-6);
		return value;
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
