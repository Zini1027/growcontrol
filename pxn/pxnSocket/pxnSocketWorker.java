package com.poixson.pxnSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketWorker {

	protected final Socket socket;
	protected final pxnSocketProcessor processor;
	protected boolean closed = false;
	protected final int socketId;

	// input/output threads
	protected final Thread threadReader;
	protected final Thread threadSender;


	public pxnSocketWorker(Socket socket, pxnSocketProcessor processor) {
		if(socket    == null) throw new NullPointerException("socket cannot be null!");
		if(processor == null) throw new NullPointerException("processor cannot be null!");
		this.socket = socket;
		socketId = getNextId();
		this.processor = processor;
		try {
			socket.setKeepAlive(true);
		} catch (SocketException e) {
			pxnLogger.getLogger().exception(e);
		}
		// reader thread
		threadReader = new pxnSocketWorkerReader(this, socket, processor.getInputQueue());
		// sender thread
		threadSender = new pxnSocketWorkerSender(this, socket, processor.getOutputQueue());
		// start threads
		threadReader.start();
		threadSender.start();
	}


	// add to output queue
	public void sendData(String line) throws Exception {
		processor.sendData(line);
	}


	// get next id
	private static int nextId = 0;
	public static synchronized int getNextId() {
		nextId++;
		return nextId - 1;
	}
	// get socket id
	public int getSocketId() {
		return socketId;
	}


	// close socket
	public void close() {
		if(closed) return;
		closed = true;
		// clear out queue
		processor.getOutputQueue().clear();
		// close socket
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
pxnLogger.getLogger().exception(e);
			}
		}
		// stop processor
		processor.interrupt();
		pxnLogger.getLogger().info("Disconnected: "+getIPString());
		// stop input/output threads
		threadReader.interrupt();
		threadSender.interrupt();
	}
	// is connected / closed
	public boolean isClosed() {
		if(socket == null || socket.isClosed())
			closed = true;
		return closed;
	}


	// get ip address
	public InetAddress getIP() {
		if(socket == null) return null;
		return socket.getInetAddress();
	}
	public String getIPString() {
		if(socket == null) return null;
		InetAddress ip = getIP();
		if(ip == null) return null;
		return ip.getHostAddress().toString();
	}


}
