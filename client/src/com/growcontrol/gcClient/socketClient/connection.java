package com.growcontrol.gcClient.socketClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.growcontrol.gcClient.socketClient.packets.clientPacket;

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
			out = new PrintWriter(socket.getOutputStream(), false);
		} catch(UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}


	public boolean sendPacket(clientPacket packet) {
		if(socket == null || !socket.isConnected()) return false;
		out.print(clientPacket.EOL);
		out.print(packet.getPacketString());
		out.print(clientPacket.EOL);
		out.flush();
		return false;
	}


	public boolean isConnected() {
		return false;
	}


}
