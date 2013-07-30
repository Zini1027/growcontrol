package com.growcontrol.gcServer.socketServer;

import java.util.List;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnSocket.worker.pxnSocketWorker;
import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginManager;


public final class gcPacketSender {
	private gcPacketSender() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}


	// HEY <server-version>
	// (login is ok)
	public static void sendHEY(pxnSocketWorker worker, String serverVersion) {
		if(serverVersion == null) throw new NullPointerException("serverVersion can't be null!");
		worker.sendData("HEY "+serverVersion);
pxnLogger.get().severe("Sent HEY packet!");
	}


	// LIST zones
	// (list loaded zones)
	public static void sendLISTZones(pxnSocketWorker worker) {
		List<String> zones = gcServer.get().getZones();
		for(String zoneName : zones)
			sendZONE(worker, zoneName);
pxnLogger.get().severe("Sent ZONE packets!");
	}
	// ZONE
	// (send a zone)
	public static void sendZONE(pxnSocketWorker worker, String zoneName) {
		worker.sendData("ZONE "+zoneName);
	}


	// LIST client plugins
	// (list loaded client plugins)
	// 0 | plugin name
	// 1 | version
	// 2 | filename
	public static void sendLISTPluginsClient(pxnSocketWorker worker) {
		List<String[]> clientPlugins = gcServerPluginManager.get().getClientPlugins();
		for(String[] info : clientPlugins) {
			sendPLUGIN(worker, info[2]);
		}
pxnLogger.get().severe("Sent PLUGIN packets!");
	}
	// PLUGIN
	// (send a plugin)
	public static void sendPLUGIN(pxnSocketWorker worker, String pluginName) {
		worker.sendData("PLUGIN "+pluginName);
	}


}
