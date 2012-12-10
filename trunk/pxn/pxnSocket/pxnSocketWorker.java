package com.poixson.pxnSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketWorker {

	protected final Socket socket;
	protected final pxnSocketProcessor processor;
	protected boolean closed = false;

	// input/output threads
	protected final Thread threadIn;
	protected final Thread threadOut;
	// buffers
	protected BufferedReader in  = null;
	protected PrintWriter    out = null;
	// input/output queue
	protected BlockingQueue<String> queueIn;
	protected BlockingQueue<String> queueOut;


	public pxnSocketWorker(Socket socket, pxnSocketProcessor processor) {
System.out.println("CONNECTED!!!!!!!!!!!!");
		if(socket    == null) throw new NullPointerException("socket cannot be null!");
		if(processor == null) throw new NullPointerException("processor cannot be null!");
		this.socket = socket;
		this.processor = processor;
		try {
			socket.setKeepAlive(true);
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
		// get queues
		queueIn  = processor.getInputQueue();
		queueOut = processor.getOutputQueue();
queueOut.offer("FIRST PACKET");
queueOut.offer("SECOND PACKET");
		// reader thread
		threadIn = new Thread() {
			@Override
			public void run() {
				doReaderThread();
			}
		};
		threadIn.setName("Socket Reader");
		// sender thread
		threadOut = new Thread() {
			@Override
			public void run() {
				doSenderThread();
			}
		};
		threadIn.setName("Socket Sender");
		// start threads
		threadOut.start();
		threadIn.start();
	}


	// reader thread
	private void doReaderThread() {
		pxnLogger.log().info("Connected: "+getIPString());
		String line = "";
		while(!closed) {
			try {
				line = in.readLine();
			} catch (SocketException ignore) {
pxnLogger.log().exception(ignore);
				break;
			} catch (IOException e) {
				e.printStackTrace();
				pxnLogger.log().exception(e);
				break;
			}
			if(line == null) break;
			if(line.isEmpty()) continue;
			processor.processData(line);
		}
		close();
	}
	// sender thread
	private void doSenderThread() {
		while(!closed) {
			try {
				out.println(queueOut.take());
				out.flush();
			} catch (InterruptedException e) {
				pxnLogger.log().exception(e);
			}
		}
	}
	public void sendData(String line) {
		processor.sendData(line);
	}


	// close socket
	public void close() {
		if(closed) return;
		closed = true;
		queueOut.clear();
		pxnLogger.log().info("Disconnected: "+getIPString());
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				pxnLogger.log().exception(e);
			}
		}
	}
	// is connected / closed
	public boolean isClosed() {
		if(socket == null || in == null || out == null)
			closed = true;
		else if(socket.isClosed() || out.checkError())
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
