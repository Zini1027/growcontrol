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
		if(version == null) throw new NullPointerException("version cannot be null");
		String hey = "HEY "+version;
		serverPacket packet = new serverPacket(hey+EOL);
		return packet;
	}


}
