package com.growcontrol.gcServer.socketServer;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketProcessor;
import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.gcPluginYML;


public class sendServerPackets {


	// HEY <server-version>
	// (login is ok)
	public static void sendHEY(pxnSocketProcessor processor, String serverVersion) throws Exception {
		if(serverVersion == null) throw new NullPointerException("serverVersion can't be null!");
		processor.sendData("HEY "+serverVersion);
pxnLogger.get().severe("Sent HEY packet!");
	}


	// LIST zones
	// (list loaded zones)
	public static void sendLISTZones(pxnSocketProcessor processor) throws Exception {
		List<String> zones = gcServer.get().getZones();
		for(String zoneName : zones)
			sendZONE(processor, zoneName);
pxnLogger.get().severe("Sent ZONE packets!");
	}
	// ZONE
	// (send a zone)
	public static void sendZONE(pxnSocketProcessor processor, String zoneName) throws Exception {
		processor.sendData("ZONE "+zoneName);
	}


	// LIST client plugins
	// (list loaded client plugins)
	public static void sendLISTPluginsClient(pxnSocketProcessor processor) throws Exception {
		for(Entry<String, File> entry : gcPluginYML.clientMainClasses.entrySet()) {
			sendPLUGIN(processor, entry.getValue().getName() );
		}
pxnLogger.get().severe("Sent PLUGIN packets!");
	}
	// PLUGIN
	// (send a plugin)
	public static void sendPLUGIN(pxnSocketProcessor processor, String pluginName) throws Exception {
		processor.sendData("PLUGIN "+pluginName);
	}


}
