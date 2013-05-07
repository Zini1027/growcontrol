package com.growcontrol.gcClient.socketClient;

import com.gcCommon.pxnSocket.pxnSocketProcessor;


public class sendClientPackets {


	// HELLO <client-version> [<username> [<password>]]
	// (login packet)
	public static void sendHELLO(pxnSocketProcessor processor, String clientVersion) throws Exception{
		sendHELLO(processor, clientVersion, null, null);
	}
	public static void sendHELLO(pxnSocketProcessor processor, String clientVersion, String username, String password) throws Exception {
		if(clientVersion == null) throw new NullPointerException("clientVersion can't be null!");
		String packet = "HELLO "+clientVersion;
		if(username != null && !username.isEmpty()) {
			packet += " "+username;
			if(password != null && !password.isEmpty()) {
				packet += " "+password;
			}
		}
		processor.sendData(packet);
System.out.println("Sent HEY packet! "+clientVersion);
	}


	// LIST zones
	// LIST plugins client
	// (client list requests)
	public static void sendLIST(pxnSocketProcessor processor, String args) throws Exception {
		if(args == null) throw new NullPointerException("LIST args can't be null!");
		if(args.isEmpty()) throw new IllegalArgumentException("LIST args can't be empty!");
		processor.sendData("LIST "+args);
System.out.println("Sent LIST packet! "+args);
	}


}
