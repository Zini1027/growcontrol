package com.poixson.pxnSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import com.poixson.pxnLogger.pxnLogger;


public class pxnSocketWorker {

	protected final Socket socket;
	protected final pxnSocketProcessor processor;
//	protected boolean closed = false;

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
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
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
//		pxnLogger.log().info("Connected: "+getIPString());
		String line = "";
//		while(!closed) {
		while(true) {
System.out.println("READER THREAD RUNNING");
			try {
				line = in.readLine();
				if(line == null) break;
			} catch (SocketException ignore) {
				break;
			} catch (IOException e) {
				e.printStackTrace();
				pxnLogger.log().exception(e);
				break;
			}
			if(!line.isEmpty())
				processor.process(line);
		}
//		close();
//		pxnLogger.log().info("Disconnected: "+getIPString());
//		outQueue.clear();
		if(socket != null && !socket.isClosed()) {
			try {
				socket.close();
			} catch (IOException e) {
				pxnLogger.log().exception(e);
			}
		}
	}
	// sender thread
	private void doSenderThread() {
System.out.println("SENDER THREAD RUNNING");
//		while(!closed) {
//			try {
//out.println(queueOut.take());
//			} catch (InterruptedException e) {
//				gcServer.log.exception(e);
//			}
//		}
	}


//		// packet processor
//		processor = new Processor();
//		queueOut = processor.getQueueOut();


//	// close socket
//	public void close() {
//		if(socket == null) return;
//		try {
//			socket.close();
//		} catch (IOException e) {
//			gcServer.log.exception(e);
//		}
//	}
//	// is connected/closed
//	public boolean isClosed() {
//		if(socket == null || in == null || out == null)
//			closed = true;
//		else if(socket.isClosed() || out.checkError())
//			closed = true;
//		return closed;
//	}
//
//
//	// get remote ip address
//	public InetAddress getIP() {
//		if(socket == null) return null;
//		return socket.getInetAddress();
//	}
//	public String getIPString() {
//		InetAddress ip = getIP();
//		if(ip == null) return null;
//		return ip.toString().replace("/", "");
//	}


}
