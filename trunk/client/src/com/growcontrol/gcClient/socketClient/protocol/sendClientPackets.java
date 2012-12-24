package com.growcontrol.gcClient.socketClient.protocol;

import com.poixson.pxnSocket.pxnSocketProcessor;


public class sendClientPackets {


	// HELLO <client-version> [<username> [<password>]]
	public static void sendHELLO(pxnSocketProcessor processor, String clientVersion) {
		sendHELLO(processor, clientVersion, null, null);
	}
	public static void sendHELLO(pxnSocketProcessor processor, String clientVersion, String username, String password) {
		if(clientVersion == null) throw new NullPointerException("clientVersion can't be null!");
		String packet = "HELLO "+clientVersion;
		if(username != null && !username.isEmpty()) {
			packet += " "+username;
			if(password != null && !password.isEmpty()) {
				packet += " "+password;
			}
		}
		processor.sendData(packet);
	}


}
