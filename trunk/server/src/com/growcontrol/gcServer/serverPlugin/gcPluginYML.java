package com.growcontrol.gcServer.serverPlugin;

import java.io.File;
import java.util.HashMap;

import com.growcontrol.gcCommon.pxnPlugin.pxnPluginYML;


public class gcPluginYML extends pxnPluginYML {

	// client plugins list
	public static HashMap<String, File> clientMainClasses = new HashMap<String, File>();


	public gcPluginYML(File file, String fileName) {
		super(file, fileName);
		// save client main class for later
		String clientMainClass = getMainClassValue("Client Main");
		if(clientMainClass != null && !clientMainClass.isEmpty())
			clientMainClasses.put(clientMainClass, file);
	}


	// main class name
	public String getMainClassValue() {
		return getMainClassValue("Server Main");
	}
	public String getMainClassValue(String mainClassName) {
		if(config == null) return null;
		// get main class from plugin.yml
		String mainClassValue = config.getString(mainClassName);
		if(mainClassValue == null || mainClassValue.isEmpty())
			return null;
		// trim .class from end
		if(mainClassValue.endsWith(".class"))
			mainClassValue = mainClassValue.substring(0, mainClassValue.length()-6);
		return mainClassValue;
	}


}
