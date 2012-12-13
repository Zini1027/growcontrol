package com.poixson.pxnSocket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
	protected final int socketId;

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
		if(socket    == null) throw new NullPointerException("socket cannot be null!");
		if(processor == null) throw new NullPointerException("processor cannot be null!");
		this.socket = socket;
		socketId = getNextId();
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
//queueOut.offer("FIRST PACKET");
//queueOut.offer("SECOND PACKET");
		// reader thread
		threadIn = new Thread() {
			@Override
			public void run() {
				startReaderThread();
			}
		};
		threadIn.setName("Socket-Reader-"+Integer.toString(socketId));
		// sender thread
		threadOut = new Thread() {
			@Override
			public void run() {
				startSenderThread();
			}
		};
		threadIn.setName("Socket-Sender-"+Integer.toString(socketId));
		// start threads
		threadOut.start();
		threadIn.start();
	}


	// reader thread
	private void startReaderThread() {
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
	private void startSenderThread() {
		while(!closed) {
			try {
				String line = queueOut.take();
				if(line.startsWith("SENDFILE:")) {
					// send file
					sendFileNow(line.substring(9).trim());
				} else {
					// send string
					out.println(line);
					out.flush();
				}
			} catch (InterruptedException e) {
				pxnLogger.log().exception(e);
			}
		}
	}
	// send file (from sender thread)
	private void sendFileNow(String fileName) {
		pxnLogger.log().info("Sending file: "+fileName);
		File file = new File(fileName);
		final int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		try {
			FileInputStream fileStream = new FileInputStream(file);
			BufferedInputStream inputStream = new BufferedInputStream(fileStream);
			OutputStream outputStream = socket.getOutputStream();
//			while(true) {
				inputStream.read(buffer, 0, buffer.length);
				outputStream.write(buffer, 0, buffer.length);
//			}
			inputStream.close();
			inputStream = null;
			fileStream = null;
			file = null;
		} catch (FileNotFoundException e) {
			pxnLogger.log().exception(e);
		} catch (IOException e) {
			pxnLogger.log().exception(e);
		}
	}
	// add to output queue
	public void sendData(String line) {
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
