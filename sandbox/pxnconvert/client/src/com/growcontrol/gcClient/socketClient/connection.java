package com.growcontrol.gcClient.socketClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.gcClient.ConnectState;
import com.growcontrol.gcClient.socketClient.packets.clientPacket;

public class connection {

	protected final String host;
	protected final int    port;

	protected Socket socket = null;
	protected InputStreamReader in = null;
	protected PrintWriter      out = null;


	public connection(String host, int port) {
		if(host == null) throw new NullPointerException();
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
			JOptionPane.showMessageDialog(null, e.getMessage(), "Connection failed!", JOptionPane.ERROR_MESSAGE);
			gcClient.setConnectState(ConnectState.CLOSED);
			return;
		}
	}
	// close socket
	public void finalize() {
		try {
			close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void close() throws IOException {
		if(socket == null) return;
		if(socket.isConnected() || !socket.isClosed())
			socket.close();
		socket = null;
	}


	public boolean sendPacket(clientPacket packet) {
		if(packet == null) throw new NullPointerException();
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
