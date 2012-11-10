package com.growcontrol.gcServer.socketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.growcontrol.gcServer.gcServer;

public class socketWorker implements Runnable {

	protected Socket socket;
	protected Thread thread;
	protected BufferedReader in  = null;
	protected PrintWriter    out = null;
	protected boolean closed = false;


	public socketWorker(Socket socket) {
		this.socket = socket;
		try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
		thread = new Thread(this);
		thread.start();
	}


	// listen for incomming data
	@Override
	public void run() {
		while(!closed) {
			try {
				int bit = in.read();
				if(bit == -1) {
					closed = true;
					break;
				}
				gcServer.log.warning("GOT "+ ((char)bit) );
			} catch (IOException e) {
				gcServer.log.exception(e);
			}
		}
		gcServer.log.info(getIPString()+" disconnected");
	}


	// is connected/closed
	public boolean isClosed() {
		if(socket == null || in == null || out == null)
			closed = true;
		if(socket.isClosed() || out.checkError())
			closed = true;
		return closed;
	}


	// get remote ip address
	public InetAddress getIP() {
		if(socket == null) return null;
		return socket.getInetAddress();
	}
	public String getIPString() {
		InetAddress ip = getIP();
		if(ip == null) return null;
		return ip.toString().replace("/", "");
	}


}
