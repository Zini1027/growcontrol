package com.growcontrol.arduinogc.interfaces;

import java.net.Socket;
import java.util.Enumeration;

import com.growcontrol.arduinogc.ArduinoGC;

public class ArduinoNet extends Thread implements ArduinoInterface {
@SuppressWarnings("unused")
	private static final int THREAD_HEARTBEAT = 50;
@SuppressWarnings("unused")
	private static final int THREAD_SLEEP = 100;

	protected final String host;
	protected final int port;

	protected Socket client = null;
//	protected String buffer
	protected boolean ready = false;


	public ArduinoNet(String host, int port) {
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
		// initialize arduino
		this.host = host;
		this.port = port;
		//sendMessage("reset");
		this.start();
	}


	@Override
	public boolean isReady() {
		return false;
	}


	// data retrieval thread
	public void run() {
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
//		while(!GrowControl.stopping) {
//			try {
//				Thread.sleep(THREAD_SLEEP);
//			} catch (InterruptedException e) {
//				GrowControl.log.exception(e);
//			}
//			checkSending();
//		}
	}


}
