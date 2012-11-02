package com.growcontrol.gcClient.protocol.packets;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.growcontrol.gcClient.gcClient;

public class clientPacketHello extends packetControl {

	public final String username;
	public final String password;
	public final String version;


	public clientPacketHello(String version, String username, String password) {
		this.version  = version;
		this.username = username;
		if(password != null) {
			try {
				password = gcClient.md5(password);
			} catch(NoSuchAlgorithmException e) {
				password = null;
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				password = null;
				e.printStackTrace();
			} catch(Exception e) {
				password = null;
				e.printStackTrace();
			}
		}
		this.password = password;
	}


	public String getPacketString() {
		String hello = "HELLO "+version;
		if(username != null && !username.isEmpty()) {
			hello += " "+username;
			if(password != null && !password.isEmpty())
				hello += " "+password;
		}
		return hello+EOL;
	}


}
