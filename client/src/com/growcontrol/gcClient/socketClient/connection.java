package com.growcontrol.gcClient.protocol;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.growcontrol.gcClient.protocol.packets.packet;

public class connection {

	protected final String host;
	protected final int    port;

	protected Socket socket = null;
	protected InputStreamReader in = null;
	protected PrintWriter      out = null;


	public connection(String host, int port) {

		this.host = host;
		this.port = port;
		try {
			socket = new Socket(host, port);
			in = new InputStreamReader(socket.getInputStream());
                                       //boolean is for auto flush - will turn off
			out = new PrintWriter(socket.getOutputStream(), false);
		} catch(UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}


	public boolean sendPacket(packet p) {
		if(socket == null || !socket.isConnected()) return false;
		out.print(packet.EOL);
		out.print(p.getPacketString());
		out.print(packet.EOL);
		out.flush();
		return false;
	}


	public boolean isConnected() {
		return false;
	}


}
