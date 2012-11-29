package com.growcontrol.gcServer.socketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.growcontrol.gcServer.gcServer;


public class socketWorker implements Runnable {

	protected Socket socket;
	protected Thread thread;
	protected boolean closed = false;

	protected BufferedReader in  = null;
	protected PrintWriter    out = null;
	protected Queue<String> inQueue  = new ConcurrentLinkedQueue<String>();
//	protected Queue<String> outQueue = new ConcurrentLinkedQueue<String>();


	public socketWorker(Socket socket) {
		if(socket == null) throw new NullPointerException("socket cannot be null");
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
		gcServer.log.info("Connected: "+getIPString());
		String line = "";
		while(!closed) {
			try {
				line = in.readLine();
				if(line == null) break;
			} catch (SocketException ignore) {
				break;
			} catch (IOException e) {
				e.printStackTrace();
				gcServer.log.exception(e);
				break;
			}
			line = line.trim();
			if(line.isEmpty()) continue;
			inQueue.add(line);
		}
		close();
		gcServer.log.info("Disconnected: "+getIPString());
//		outQueue.clear();
		socket = null;
		in  = null;
		out = null;
	}


	// close socket
	public void close() {
		if(socket == null) return;
		try {
			socket.close();
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
	}
	// is connected/closed
	public boolean isClosed() {
		if(socket == null || in == null || out == null)
			closed = true;
		else if(socket.isClosed() || out.checkError())
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
