package com.growcontrol.gcServer.socketServer.packets;

public class serverPacket {

	public String packetString = null;
	public static final String EOL = "\r\n";


	public serverPacket() {
	}
	public serverPacket(String packetString) {
		this.packetString = packetString;
	}


	public String getPacketString() {
		if(packetString != null) return packetString;
		return "UNKNOWN PACKET"+EOL;
	};


	// HEY <server version>
	public static serverPacket sendHEY(String version) {
		String hey = "HEY "+version;
		serverPacket packet = new serverPacket(hey+EOL);
		return packet;
	}


}
