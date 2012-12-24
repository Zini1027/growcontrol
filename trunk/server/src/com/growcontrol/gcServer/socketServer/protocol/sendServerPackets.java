package com.growcontrol.gcServer.socketServer.protocol;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import com.growcontrol.gcServer.Main;
import com.growcontrol.gcServer.logger.gcLogger;
import com.growcontrol.gcServer.serverPlugin.gcPluginYML;
import com.poixson.pxnSocket.pxnSocketProcessor;


public class sendServerPackets {


	// HEY <server-version>
	public static void sendHEY(pxnSocketProcessor processor, String serverVersion) {
		if(serverVersion == null) throw new NullPointerException("serverVersion can't be null!");
		processor.sendData("HEY "+serverVersion);
gcLogger.getLogger().severe("Sent HEY packet!");
	}


	// LIST zones
	public static void sendLISTZones(pxnSocketProcessor processor) {
		List<String> zones = Main.getServer().getZones();
		for(String zoneName : zones)
			sendZONE(processor, zoneName);
gcLogger.getLogger().severe("Sent ZONE packets!");
	}
	// ZONE
	public static void sendZONE(pxnSocketProcessor processor, String zoneName) {
		processor.sendData("ZONE "+zoneName);
	}


	// LIST client plugins
	public static void sendLISTPluginsClient(pxnSocketProcessor processor) {
		for(Entry<String, File> entry : gcPluginYML.clientMainClasses.entrySet()) {
			sendPLUGIN(processor, entry.getKey());
		}
gcLogger.getLogger().severe("Sent PLUGIN packets!");
	}
	// PLUGIN
	public static void sendPLUGIN(pxnSocketProcessor processor, String pluginName) {
		processor.sendData("PLUGIN "+pluginName);
	}


}
