package com.growcontrol.gcCommon.pxnSocket;


public final class pxnSocketUtils {
	private pxnSocketUtils() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static final int MAXPORT = 65536;

	public enum pxnSocketState {CLOSED, WAITING, CONNECTED, FAILED};


	// trim http:// and /path
	public static String prepHost(String host) {
		if(host == null || host.isEmpty())
			return null;
		if(host.contains("://")) host = host.substring(host.indexOf("://")+3);
		if(host.contains("/")) host = host.substring(0, host.indexOf("/"));
		return host.trim();
	}
	// validate port
	public static int prepPort(int port) {
		if(port < 1 || port > MAXPORT)
			return 0;
		return port;
	}


}
