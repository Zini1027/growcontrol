package com.growcontrol.gcServer.socketServer.packets;

public class serverPacket {

	public String packetString = null;
	public static final String EOL = "\r\n";


	public serverPacket() {
		this(null);
	}
	public serverPacket(String packetString) {
		if(packetString == null || packetString.isEmpty())
			this.packetString = null;
		else
			this.packetString = packetString;
	}


	public String getPacketString() {
		if(packetString != null) return packetString;
		return "UNKNOWN PACKET"+EOL;
	};


	// HEY <server version>
	public static serverPacket sendHEY(String version) {
		if(version == null) throw new NullPointerException("version cannot be null");
		String packetStr = "HEY "+version;
		serverPacket packet = new serverPacket(packetStr+EOL);
		return packet;
	}


}
