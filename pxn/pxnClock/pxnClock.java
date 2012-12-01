package com.poixson.pxnClock;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.poixson.pxnLogger.pxnLogger;


public class pxnClock extends Thread {

	protected boolean enabled  = true;
	protected String timeServer = "ntp.cais.rnp.br";

	protected double localOffset = 0.0;
	protected double lastChecked = 0.0;

	// instance lock
	protected final Object threadLock = new Object();


	// logger
	protected pxnLogger log;
	public pxnClock(pxnLogger log) {
		this.log = log;
	}
	public pxnClock() {
		log = pxnLogger.getLogger();
	}


	// static update
	public static double getLocalOffsetNow() {
		return getLocalOffsetNow(null);
	}
	public static double getLocalOffsetNow(String timeServer) {
		pxnClock clock = new pxnClock();
		if(timeServer != null && !timeServer.isEmpty())
			clock.setTimeServer(timeServer);
		clock.update(false);
		return clock.getLocalOffset();
	}


	// update from time server
	public void update(boolean enabled, boolean threaded) {
		setEnabled(enabled);
		update(threaded);
	}
	public void update(String timeServer, boolean threaded) {
		setTimeServer(timeServer);
		update(threaded);
	}
	public synchronized void update(boolean threaded) {
		if(!enabled) return;
		// still running
		if(this.isAlive()) return;
		double time = System.currentTimeMillis();
		if(lastChecked!=0.0 && (time-lastChecked)/1000.0 < 60.0) return;
		lastChecked = time;
		if(threaded) {
			// run threaded
			this.start();
		} else {
			// run blocking
			run();
		}
	}


	// run time query
	public void run() {
		if(!enabled) return;
		synchronized(threadLock) {
			try {
				DatagramSocket socket;
				socket = new DatagramSocket();
				InetAddress address = InetAddress.getByName(timeServer);
				byte[] buf = new ntpMessage().toByteArray();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 123);
				ntpMessage.encodeTimestamp(packet.getData(), 40, fromUnixTimestamp());
				socket.send(packet);
				socket.receive(packet);
				ntpMessage msg = new ntpMessage(packet.getData());
				// calculate local offset
				double time = System.currentTimeMillis();
				localOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - fromUnixTimestamp(time))) / 2.0;
				log.info("Internal time adjusted by "+ (localOffset>0?"+":"") + new DecimalFormat("0.000").format(localOffset) +" seconds");
				log.debug("System time:   "+timestampToString(time/1000.0));
				log.debug("Adjusted time: "+getTimeString());
				// clean up
				socket.close();
				msg = null;
				packet = null;
				socket = null;
				address = null;
				return;
//double destinationTimestamp = fromUnixTimestamp();
//double roundTripDelay = (destinationTimestamp-msg.originateTimestamp) - (msg.transmitTimestamp-msg.receiveTimestamp);
//double localClockOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - destinationTimestamp)) / 2;
//GrowControl.log.severe(msg.toString());
//System.out.println("Dest. timestamp:     " + NtpMessage.timestampToString(destinationTimestamp));
//System.out.println("Round-trip delay: " + new DecimalFormat("0.00").format(roundTripDelay*1000) + " ms");
//System.out.println("Local clock offset: " + new DecimalFormat("0.00").format(localClockOffset*1000) + " ms");
			} catch (UnknownHostException e) {
				log.exception(e);
//			} catch (SocketException e) {
//				GrowControl.log.exception(e);
//			} catch (IOException e) {
//				GrowControl.log.exception(e);
			} catch (Exception e) {
				log.exception(e);
			}
		}
	}
	// has update run?
	public boolean hasUpdated() {
		if(!enabled) return true;
		return (localOffset != 0.0) && (lastChecked != 0.0);
	}


	// convert from 1970ms to 1900s
	protected static double fromUnixTimestamp(double timestamp) {
		return (timestamp/1000.0) + 2208988800.0;
	}
	protected static double fromUnixTimestamp() {
		return fromUnixTimestamp(System.currentTimeMillis());
	}


	// display time formatted: 29-Jun-2012 03:07:04.794
	public static String timestampToString(double timestamp) {
		if(timestamp <= 0.0) return "0";
		return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date( (long) (timestamp*1000.0) )) +
			new DecimalFormat(".000").format( timestamp - ((long) timestamp) );
	}


	// get corrected time
	public double getLocalOffset() {
		return localOffset;
	}
	public double getTimeSeconds() {
		return getTimeMillis() / 1000.0;
	}
	public long getTimeMillis() {
		return System.currentTimeMillis() + (long)(localOffset * 1000.0);
	}
	public String getTimeString() {
		return timestampToString(getTimeSeconds());
	}


	// enable/disable NTP
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(!enabled) {
			localOffset = 0.0;
			lastChecked = 0.0;
		}
	}


	// set time server host
	public void setTimeServer(String timeServer) {
		if(timeServer == null || timeServer.isEmpty()) throw new NullPointerException("timeServer host can't be null!");
		this.timeServer = timeServer;
	}


}
