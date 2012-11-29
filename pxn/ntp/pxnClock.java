package com.poixson.ntp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.growcontrol.gcServer.gcServer;


public class pxnClock extends Thread {

	private static boolean usingNTP = false;
	protected static double localOffset = 0.0;
	protected static double lastChecked = 0.0;

	// instance thread
	protected static final pxnClock instance = new pxnClock();


	public static void updateNTP_Blocking() {
		updateNTP(false);
	}
	public static void updateNTP_Threaded() {
		updateNTP(true);
	}
	public static synchronized void updateNTP(boolean threaded) {
		if(!usingNTP) return;
		if(instance.isAlive()) return;
		double time = System.currentTimeMillis();
		if(lastChecked!=0.0 && (time-lastChecked)/1000.0<60.0) return;
		lastChecked = time;
		if(threaded)
			instance.start();
		else
			doTimeQuery();
	}


	// run time query
	private static synchronized void doTimeQuery() {
		if(!usingNTP) return;
		try {
			DatagramSocket socket;
			socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName("ntp.cais.rnp.br");
			byte[] buf = new ntpMessage().toByteArray();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 123);
			ntpMessage.encodeTimestamp(packet.getData(), 40, fromUnixTimestamp());
			socket.send(packet);
			socket.receive(packet);
			ntpMessage msg = new ntpMessage(packet.getData());
			// calculate local offset
			double time = System.currentTimeMillis();
			localOffset = ((msg.receiveTimestamp - msg.originateTimestamp) + (msg.transmitTimestamp - fromUnixTimestamp(time))) / 2.0;
			gcServer.log.info("Internal time adjusted by "+ (localOffset>0?"+":"") + new DecimalFormat("0.000").format(localOffset) +" seconds");
			gcServer.log.debug("System time:   "+pxnClock.timestampToString(time/1000.0));
			gcServer.log.debug("Adjusted time: "+pxnClock.getTimeString());
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
			gcServer.log.exception(e);
//		} catch (SocketException e) {
//			GrowControl.log.exception(e);
//		} catch (IOException e) {
//			GrowControl.log.exception(e);
		} catch (Exception e) {
			gcServer.log.exception(e);
		}
	}
	// run threaded
	public void run() {
		doTimeQuery();
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


	// get fixed time
	public static double getTimeSeconds() {
		return getTimeMillis() / 1000.0;
	}
	public static long getTimeMillis() {
		return System.currentTimeMillis() + (long)(localOffset * 1000.0);
	}
	public static String getTimeString() {
		return timestampToString(getTimeSeconds());
	}


	// enable/disable NTP
	public static boolean usingNTP() {
		return usingNTP;
	}
	public static void setUsingNTP(boolean enabled) {
		usingNTP = enabled;
		if(!usingNTP) {
			localOffset = 0.0;
			lastChecked = 0.0;
		}
	}


}
