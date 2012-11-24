package com.growcontrol.arduinogc.interfaces;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.growcontrol.arduinogc.ArduinoGC;
import com.growcontrol.arduinogc.DataProcessor;
import com.growcontrol.arduinogc.msgQueue;
import com.growcontrol.gcServer.pxnUtils;

public class ArduinoUSB extends ArduinoInterface {

	protected final String comPort;

	// comm port
	protected SerialPort serial = null;
	protected OutputStream out = null;
	protected InputStream in = null;

	// data buffers
	private String bufferIn = "";
	private String bufferOut = "";

	protected msgQueue queue = new msgQueue();


	public ArduinoUSB(String comPort) {
		if(comPort == null || comPort.isEmpty()) {
			ArduinoGC.log.severe("Invalid com port; not specified");
			this.comPort = null;
			return;
		}
		this.comPort = comPort;
	}


	// start / stop interface
	public void StartInterface() {
		// initialize arduino
		queue.sendRawCommand("reset");
		super.StartInterface();
	}
	public void StopInterface() {
		super.StopInterface();
	}


	// communication thread
	public void run() {
		while(!stopping) {
			// send from queue
			sendMessages();
			// check for incoming data
//			checkSending();
			// sleep thread
			pxnUtils.Sleep(THREAD_WAIT);
		}
		serial.close();
		out = null;
		in = null;
		serial = null;
	}


	protected void connect() {
		if(comPort == null || comPort.isEmpty()) return;
		CommPortIdentifier commId = null;
		// valid com port
		try {
			commId = CommPortIdentifier.getPortIdentifier(comPort);
		} catch (NoSuchPortException e) {
			ArduinoGC.log.severe("Arduino comm port not found! "+comPort);
			ArduinoGC.log.exception(e);
			return;
		}
		if(commId == null) return;
		// port in use
		if(commId.isCurrentlyOwned()) {
			ArduinoGC.log.severe("Arduino comm port is already in use! "+comPort);
			return;
		}
		// open com port
		CommPort comm = null;
		try {
			comm = commId.open(ArduinoGC.class.getName(), 2000);
		} catch (PortInUseException e) {
			ArduinoGC.log.severe("Failed to open comm port! "+comPort);
			ArduinoGC.log.exception(e);
			return;
		}
		if(comm == null) return;
		// not a serial port
		if(!(comm instanceof SerialPort)) {
			ArduinoGC.log.severe("Only serial ports are currently supported! "+comPort);
			return;
		}
		try {
			serial = (SerialPort) comm;
			// configure port
			serial.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			in = serial.getInputStream();
			out = serial.getOutputStream();
//			try {
//				serial.addEventListener(this);
//				serial.notifyOnDataAvailable(true);
//			} catch (TooManyListenersException e) {
//				ArduinoGC.log.exception(e);
//			}
//			// reader/writer threads
//			(new Thread(new SerialReader( serial.getInputStream())  )).start();
//			(new Thread(new SerialWriter( serial.getOutputStream()) )).start();
		} catch (UnsupportedCommOperationException e) {
			ArduinoGC.log.severe("Failed to open comm port! "+comPort);
			ArduinoGC.log.exception(e);
			return;
		} catch (IOException e) {
			ArduinoGC.log.severe("Failed to open comm port! "+comPort);
			ArduinoGC.log.exception(e);
			return;
		}
	}


	protected synchronized void sendMessages() {
		// nothing in queue
		if(queue.countNeedSending() == 0) return;
		// get data to send
		synchronized(bufferOut) {
			if(bufferOut == null || bufferOut.isEmpty())
				bufferOut = queue.getMessages()+"\r\n";
		}
		// connect to arduino
		connect();
		if(serial==null) {
			ArduinoGC.log.severe("Not connected!");
			return;
		}
		// add padding
		if(bufferOut.length() < 10) bufferOut += "\r\n          ";
		// send command
		ArduinoGC.log.debug("Sending "+Integer.toString(bufferOut.length())+" bytes");
		try {
			out.write(bufferOut.getBytes());
//			out.flush();
		} catch (IOException e) {
			ArduinoGC.log.exception(e);
		}
		ArduinoGC.log.debug("Waiting for reply..");
		// sleep one heartbeat
		pxnUtils.Sleep(THREAD_HEARTBEAT);
		int timeout = 2000;
		while(serial!=null && in!=null &&
				!stopping && timeout>=0) {
			// check got data
			if(checkAvailable()) timeout = 100;
			timeout -= THREAD_HEARTBEAT;
			// sleep one heartbeat
			pxnUtils.Sleep(THREAD_HEARTBEAT);
		}
//		reset();
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
try {
	ArduinoGC.log.severe(Integer.toString(in.available()));
} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
boolean b = true;if(b)return false;
		if(in == null) return false;
		try {
			boolean gotData = false;
			int len = 0;
			byte[] data = new byte[1024];
			while( (len=in.read(data)) > -1) {
				ArduinoGC.log.info("Got "+Integer.toString(len)+" bytes of data");
//ArduinoGC.log.warning(Integer.toString(data));
				bufferIn += data;
				gotData = true;
			}
			return gotData;
		} catch (IOException e) {
			ArduinoGC.log.exception(e);
//			reset();
		}
		return false;
	}


//	// reset socket
//	private void reset() {
//		synchronized(client) {
//			try {
//				if(out    != null) out.close();
//			} catch (IOException ignore) {}
//			try {
//				if(in     != null) in.close();
//			} catch (IOException ignore) {}
//			try {
//				if(client != null) client.close();
//			} catch (IOException ignore) {}
//			in = null;
//			out = null;
//			client = null;
//		}
//	}


//try {
//@SuppressWarnings("unused")
//	Serial serial = new Serial();
//GrowControl.log.warning( Serial.list().toString() );
//} catch (SerialException e) {
//	e.printStackTrace();
//}


//	public class SerialReader implements Runnable {
//		private final InputStream in;
//		public SerialReader(InputStream in) {
//			this.in = in;
//		}
//		@Override
//		public void run() {
//			byte[] buffer = new byte[1024];
//			int len = -1;
//			while(plugin.isEnabled()) {
//				try {
//					if( (len=this.in.read(buffer)) < 0) break;
//				} catch (IOException e) {
//					ArduinoGC.log.severe("Failed to read from comm port! "+comPort);
//					ArduinoGC.log.exception(e);
//					return;
//				}
//				ArduinoGC.log.severe(new String(buffer, 0, len));
//			}
//		}
//	}
//	public class SerialWriter implements Runnable {
//		private final OutputStream out;
//		public SerialWriter(OutputStream out) {
//			this.out = out;
//		}
//		@Override
//		public void run() {
//			while(plugin.isEnabled()) {
//				int c = 0;
//				for(byte b : bufferOut.getBytes()) {
//					try {
//						this.out.write(b);
//					} catch (IOException e) {
//						ArduinoGC.log.exception(e);
//					}
//				}
//			}
//		}
//	}


//	@Override
//	public void run() {
//		while(plugin.isEnabled()) {
//			// send from queue
//			sendMessages();
//			// check for incoming data
////			checkSending();
//			// sleep thread
//			ArduinoGC.Sleep(THREAD_WAIT);
//		}
//	}


}
