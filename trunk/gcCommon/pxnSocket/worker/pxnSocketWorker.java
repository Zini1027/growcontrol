package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessor;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;


public class pxnSocketWorker {

	private final int socketId;
	private final Socket socket;
	private final pxnSocketProcessor processor;

	// input/output threads
	private final Thread threadReader;
	private final Thread threadSender;


	public pxnSocketWorker(Socket socket, pxnSocketProcessorFactory factory) {
		if(socket  == null) throw new NullPointerException("socket cannot be null!");
		if(factory == null) throw new NullPointerException("factory cannot be null!");
		this.socket = socket;
		this.processor = factory.newProcessor();
		if(this.processor == null) throw new NullPointerException("Failed to get socket processor!");
		this.socketId = this.processor.getNextId();
		try {
			socket.setKeepAlive(true);
		} catch (SocketException ignore) {}
		// input/output threads
		threadReader = new pxnSocketReader(this, socket);
		threadSender = new pxnSocketSender(this, socket);
	}


	// start socket worker
	public void Start() {
		synchronized(socket) {
			// start threads
			threadReader.start();
			threadSender.start();
		}
	}


	// close socket
	public void Close() {
		synchronized(socket) {
			if(isClosed()) return;
			// clear data queues
			processor.Closing();
			// close socket
			try {
				socket.close();
			} catch (IOException e) {
				pxnLogger.get().exception("Failed to close socket worker", e);
			}
			pxnLogger.get().info("Disconnected: "+getIPString());
//			// stop input/output threads
//			threadReader.interrupt();
//			threadSender.interrupt();
		}
	}
	public boolean isClosed() {
		return socket.isClosed();
	}


	// add to output queue
	public void sendData(String line) throws Exception {
		processor.sendData(line);
	}


	// get ip address
	public InetAddress getIP() {
		//if(socket == null) return null;
		return socket.getInetAddress();
	}
	public String getIPString() {
		//if(socket == null) return null;
		InetAddress ip = getIP();
		if(ip == null) return null;
		return ip.getHostAddress().toString();
	}


	// get socket id
	public int getSocketId() {
		return socketId;
	}


}
