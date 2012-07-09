package com.growcontrol.arduinogc.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.growcontrol.arduinogc.ArduinoGC;
import com.growcontrol.arduinogc.DataProcessor;
import com.growcontrol.arduinogc.msgQueue;
import com.growcontrol.gcServer.gcServer;

public class ArduinoNet extends Thread implements interfaceArduino {
	private static final int THREAD_HEARTBEAT = 50;
	private static final int THREAD_WAIT = 100;

	protected final String host;
	protected final int port;

	// socket
	protected Socket client = null;
	protected OutputStream out = null;
	protected InputStream in = null;

	// data buffers
	private String bufferIn = "";
	private String bufferOut = "";

	protected msgQueue queue = new msgQueue();
	private boolean ready = false;
	protected final ArduinoGC plugin;


	public ArduinoNet(ArduinoGC plugin, String host, int port) {
		this.plugin = plugin;
		if(host == null || host.isEmpty()) {
			ArduinoGC.log.severe("Invalid host; not specified");
			this.host = null; this.port = 0;
			return;
		}
		if(port < 1) {
			ArduinoGC.log.severe("Invalid port for host: "+host+":"+Integer.toString(port));
			this.host = null; this.port = 0;
			return;
		}
		this.host = host;
		this.port = port;
		// initialize arduino
		queue.sendRawCommand("reset");
		this.start();
	}
	public void Close() {
		reset();
	}
	// arduino is ready
	@Override
	public boolean isReady() {
		return ready;
	}


	// communication thread
	public void run() {
		while(plugin.isEnabled()) {
			// send from queue
			sendMessages();
			// check for incoming data
//			checkSending();
			// sleep thread
			gcServer.Sleep(THREAD_WAIT);
		}
	}


	protected void connect() {
		if(host.isEmpty() || port<1) return;
		if(client != null && client.isConnected()) return;
		try {
			// connect to host
			client = new Socket(host, port);
			out = client.getOutputStream();
			in = client.getInputStream();
			return;
		} catch(ConnectException e) {
			ArduinoGC.log.warning("Failed to connect to arduino! "+host+":"+Integer.toString(port));
		} catch(UnknownHostException e){
			ArduinoGC.log.warning("Unknown Host - "+host+":"+Integer.toString(port)+" - "+e.getMessage());
//		} catch(IOException e) {
//			ArduinoGC.log.warning("Failed to connect to arduino!");
//			ArduinoGC.log.exception(e);
		} catch(Exception e) {
			ArduinoGC.log.severe("Problem initializing client socket!");
			ArduinoGC.log.exception(e);
		}
		reset();
	}


	protected synchronized void sendMessages() {
		// socket in use
		if(client != null) return;
		// nothing in queue
		if(queue.countNeedSending() == 0) return;
		// get data to send
		synchronized(bufferOut) {
			if(bufferOut == null || bufferOut.isEmpty())
				bufferOut = queue.getMessages()+"\r\n";
		}
		// connect to arduino
		connect();
		if(client==null || !client.isConnected()) {
			ArduinoGC.log.severe("Not connected!");
			return;
		}
		// add padding
		if(bufferOut.length() < 10) bufferOut += "\r\n          ";
		// send command
		ArduinoGC.log.debug("Sending "+Integer.toString(bufferOut.length())+" bytes");
		try {
			out.write(bufferOut.getBytes());
			out.flush();
		} catch (IOException e) {
			ArduinoGC.log.exception(e);
		}
		ArduinoGC.log.debug("Waiting for reply..");
		// sleep one heartbeet
		gcServer.Sleep(THREAD_HEARTBEAT);
		int timeout = 2000;
		while(client!=null && in!=null && client.isConnected()
				&& plugin.isEnabled() && timeout>=0) {
			// check got data
			if(checkAvailable()) timeout = 100;
			timeout -= THREAD_HEARTBEAT;
			// sleep one heartbeat
			gcServer.Sleep(THREAD_HEARTBEAT);
		}
		reset();
		if(bufferIn.isEmpty()) {
			ArduinoGC.log.warning("Arduino didn't answer!");
		} else {
			ArduinoGC.log.debug("Got reply "+Integer.toString(bufferIn.length())+" bytes");
			DataProcessor.processData(bufferIn);
			bufferIn = "";
			bufferOut = "";
		}
	}


	// check incoming data
	private boolean checkAvailable() {
		if(in == null) return false;
		try {
			boolean gotData = false;
			while(in.available() > 0) {
				bufferIn += (char) in.read();
				gotData = true;
			}
			return gotData;
		} catch (IOException e) {
			ArduinoGC.log.exception(e);
			reset();
		}
		return false;
	}


	// reset socket
	private void reset() {
		synchronized(client) {
			try {
				if(out    != null) out.close();
			} catch (IOException ignore) {}
			try {
				if(in     != null) in.close();
			} catch (IOException ignore) {}
			try {
				if(client != null) client.close();
			} catch (IOException ignore) {}
			in = null;
			out = null;
			client = null;
		}
	}


}
