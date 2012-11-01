package com.growcontrol.gcClient;

import com.growcontrol.gcClient.protocol.connection;
import com.growcontrol.gcClient.protocol.packetStartup;

public class gcClient {
	public static final String version = "3.0.1";
	private static gcClient client = null;
	public static connection conn = null;


	public static void main(String[] args) {
		if(client != null) throw new UnsupportedOperationException("Cannot redefine singleton gcClient; already running");
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Server");
				System.exit(0);
			}
		}
		// start gc client gui
		client = new gcClient();
	}


	public gcClient() {
		// connect to server
		conn = new connection("localhost", 1142);
		conn.sendPacket(new packetStartup());
	}


}
