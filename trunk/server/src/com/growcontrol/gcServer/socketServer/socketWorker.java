package com.growcontrol.gcServer.socketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import com.growcontrol.gcServer.gcServer;


public class socketWorker {

	protected Socket socket;
	protected Thread threadIn;
	protected Thread threadOut;
	protected boolean closed = false;

	// buffers
	protected BufferedReader in  = null;
	protected PrintWriter    out = null;

	// packet processor
	protected Processor processor;
	protected BlockingQueue<String> queueOut;


	public socketWorker(Socket socket) {
		if(socket == null) throw new NullPointerException("socket cannot be null");
		this.socket = socket;
		try {
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			gcServer.log.exception(e);
		}
		// packet processor
		processor = new Processor();
		queueOut = processor.getQueueOut();

		// reader thread
		threadIn = new Thread() {
			@Override
			public void run() {
				readerThread();
			}
		};

		// sender thread
		threadOut = new Thread() {
			@Override
			public void run() {
				senderThread();
			}
		};

		// start threads
		threadIn.start();
		threadOut.start();
	}


	// threads
	// listen for incomming data
	protected void readerThread() {
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
			processor.add(line);
		}
		close();
		gcServer.log.info("Disconnected: "+getIPString());
//		outQueue.clear();
		socket = null;
		in  = null;
		out = null;
	}
	// send outgoing data
	protected void senderThread() {
		while(!closed) {
			try {
				out.println(queueOut.take());
			} catch (InterruptedException e) {
				gcServer.log.exception(e);
			}
		}
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
