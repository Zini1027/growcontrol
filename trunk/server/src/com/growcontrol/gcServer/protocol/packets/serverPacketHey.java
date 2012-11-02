package com.growcontrol.gcClient.protocol.packets;


public class serverPacketHey extends packetControl {

	public String version = null;


	public serverPacketHey(String version) {
		this.version  = version;
	}


	public String getPacketString() {
		String hey = "HEY "+version;
		return hey+EOL;
	}


}
