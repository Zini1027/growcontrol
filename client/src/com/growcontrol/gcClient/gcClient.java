package com.growcontrol.gcClient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.growcontrol.gcClient.frames.frameLogin;
import com.growcontrol.gcClient.socketClient.connection;
import com.growcontrol.gcClient.socketClient.packets.clientPacket;

public class gcClient {
	public static final String version = "3.0.1";
	private static gcClient client = null;
	public static connection conn = null;

frameLogin login;

	public static void main(String[] args) {
new frameLogin();
boolean b = true;
if(b) return;
		if(client != null) throw new UnsupportedOperationException("Cannot redefine singleton gcClient; already running");
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Server");
				System.exit(0);
			}
		}
		// start gc client gui
		client = new gcClient();
	}


	public gcClient() {
//login = new frameLogin();
//return;
		// connect to server
		conn = new connection("192.168.3.3", 1142);
		conn.sendPacket(clientPacket.sendHELLO(version, "lorenzo", "pass"));
	}


	// md5
	public static String md5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException { 
		if(text == null) throw new NullPointerException();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return md5_convertToHex(md5hash);
	}
	private static String md5_convertToHex(byte[] data) { 
		if(data == null) throw new NullPointerException();
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if( (0 <= halfbyte) && (halfbyte <= 9) ) 
					buf.append((char)( '0'+halfbyte) );
				else
					buf.append((char)( 'a'+(halfbyte-10)) );
				halfbyte = data[i] & 0x0F;
			} while(two_halfs++ < 1);
		} 
		return buf.toString();
	}
 





}
