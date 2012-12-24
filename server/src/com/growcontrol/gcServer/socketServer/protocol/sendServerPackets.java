package com.growcontrol.gcServer.socketServer.protocol;

import com.growcontrol.gcServer.logger.gcLogger;
import com.poixson.pxnSocket.pxnSocketProcessor;


public class sendServerPackets {


	// HEY <server-version>
	public static void sendHEY(pxnSocketProcessor processor, String serverVersion) {
		if(serverVersion == null) throw new NullPointerException("serverVersion can't be null!");
		String packet = "HEY "+serverVersion;
		processor.sendData(packet);
gcLogger.getLogger().severe("Sent HEY packet!");
	}


}
