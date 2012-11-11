package com.growcontrol.gcClient.socketClient.packets;

public class clientPacket {

	public String packetString = null;
	public static final String EOL = "\r\n";


	public clientPacket() {
	}
	public clientPacket(String packetString) {
		if(packetString == null) throw new NullPointerException();
		this.packetString = packetString;
	}


	public String getPacketString() {
		if(packetString != null) return packetString;
		return "UNKNOWN PACKET"+EOL;
	};


	// HELLO <client version> [<username> [<password>]]
	public static clientPacket sendHELLO(String version) {
		return sendHELLO(version, null, null);
	}
	public static clientPacket sendHELLO(String version, String username) {
		return sendHELLO(version, username, null);
	}
	public static clientPacket sendHELLO(String version, String username, String password) {
		if(version == null) throw new NullPointerException();
		String hello = "HELLO "+version;
		if(username != null && !username.isEmpty()) {
			hello += " "+username;
			if(password != null && !password.isEmpty())
				hello += " "+password;
		}
		clientPacket packet = new clientPacket(hello+EOL);
		return packet;
	}


}
