package com.growcontrol.gcClient.clientPlugin;

import java.io.File;

import com.gcCommon.pxnPlugin.pxnPluginYML;


public class gcPluginYML extends pxnPluginYML {


	public gcPluginYML(File file, String fileName) {
		super(file, fileName);
	}


	// main class name
	public String getMainClassValue() {
		return getMainClassValue("Client Main");
	}


}
