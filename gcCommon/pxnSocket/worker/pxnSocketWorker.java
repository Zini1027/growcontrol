package com.growcontrol.gcCommon.pxnSocket.worker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import com.growcontrol.gcCommon.pxnUtils;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnParser.pxnParser;
import com.growcontrol.gcCommon.pxnSocket.pxnSocketUtils;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessor;
import com.growcontrol.gcCommon.pxnSocket.processor.pxnSocketProcessorFactory;


public class pxnSocketWorker {
	private final String logName;

	private final int socketId;
	private final Socket socket;
	private final pxnSocketProcessor processor;

	// input/output workers
	private final pxnSocketReader reader;
	private final pxnSocketSender sender;


	public pxnSocketWorker(Socket socket, pxnSocketProcessorFactory factory) {
		if(socket  == null) throw new NullPointerException("socket cannot be null!");
		if(factory == null) throw new NullPointerException("factory cannot be null!");
		logName = "SocketWorker-"+Integer.toString(getSocketId());
		this.socket = socket;
		this.processor = factory.newProcessor();
		if(this.processor == null) throw new NullPointerException("Failed to get socket processor!");
		this.socketId = pxnSocketUtils.getNextSocketId();
		try {
			socket.setKeepAlive(true);
		} catch (SocketException ignore) {}
		// input/output threads
		reader = new pxnSocketReader(this, socket);
		sender = new pxnSocketSender(this, socket);
	}


	// start socket worker
	public void Start() {
		pxnLogger.get(logName).info("Connected: ["+Integer.toString(socketId)+"] "+getIPString());
		synchronized(socket) {
			// start threads
			reader.start();
			sender.start();
//			pxnUtils.Sleep(100);
		}
	}


	// close socket
	public void Close() {
		synchronized(socket) {
			if(isClosed()) return;
			// socket closing event
			processor.Closing();
			// close socket
			try {
				socket.close();
			} catch (IOException e) {
				pxnLogger.get(logName).exception("Failed to close socket worker", e);
			}
			pxnLogger.get(logName).info("Disconnected: "+getIPString());
			// stop input/output threads
			reader.Closing();
			sender.Closing();
		}
	}
	public boolean isClosed() {
		return socket.isClosed();
	}


	// pass data to processor
	public void doProcessData(String line) {
		processor.ProcessData(this, new pxnParser(line));
	}
	// add to output queue
	public void Send(String line) {
		sender.Send(line);
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


	// socket id
	public int getSocketId() {
		return socketId;
	}


}
