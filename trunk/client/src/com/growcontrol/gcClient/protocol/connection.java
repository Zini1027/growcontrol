package com.growcontrol.gcClient.protocol;

public class connection {

	protected String host = null;
	protected int    port = 0;

	public connection(String host, int port) {
		this.host = host;
		this.port = port;
	}


	public boolean sendPacket(packet p) {
		return false;
	}


	public boolean isConnected() {
		return false;
	}


}
