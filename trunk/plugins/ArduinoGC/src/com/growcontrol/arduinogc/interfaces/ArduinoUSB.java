package com.growcontrol.arduinogc.interfaces;

import com.growcontrol.arduinogc.ArduinoGC;

public class ArduinoUSB implements ArduinoInterface {
	private static final int THREAD_HEARTBEAT = 50;
	private static final int THREAD_SLEEP = 100;

	protected final String comPort;

	protected boolean ready = false;


//try {
//@SuppressWarnings("unused")
//	Serial serial = new Serial();
//GrowControl.log.warning( Serial.list().toString() );
//} catch (SerialException e) {
//	e.printStackTrace();
//}


	public ArduinoUSB(String comPort) {
		if(comPort == null || comPort.isEmpty()) {
			ArduinoGC.log.severe("Invalid com port; not specified");
			this.comPort = null;
			return;
		}
		this.comPort = comPort;
	}


	@Override
	public boolean isReady() {
		return false;
	}


}
