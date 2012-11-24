package com.growcontrol.gcClient;

import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.growcontrol.gcClient.frames.loginFrame;
import com.growcontrol.gcClient.frames.loginHandler;
import com.growcontrol.gcClient.socketClient.connection;

public class gcClient {
	public static final String version = "3.0.1";
	public static gcClient client = null;
	private static boolean stopping = false;
	private static connection conn = null;

	// socket connection state
	public enum ConnectState {CLOSED, CONNECTING, CONNECTED, AUTHORIZED};
	private static ConnectState connectState = ConnectState.CLOSED;
	private static ConnectState lastConnectState = null;

	// frame handlers (windows)
	loginHandler loginWindow = null;;


	public static void main(String[] args) {
		if(client != null) throw new UnsupportedOperationException("Cannot redefine singleton gcClient; already running");
		for(String arg : args) {
			// version
			if(arg.equalsIgnoreCase("version")) {
				System.out.println("GrowControl "+version+" Client");
				System.exit(0);
			}
		}
		// start gc client gui
		client = new gcClient();
	}


	public gcClient() {


		while(!stopping) {
			if(connectState.equals(lastConnectState)) {
				pxnUtils.Sleep(50);
				continue;
			}
			lastConnectState = connectState;
			switch(connectState) {
			case CLOSED:
				// load login window
				if(loginWindow == null) loginWindow = new loginHandler();
				// display login card
				if(loginWindow != null) loginWindow.setDisplay(loginFrame.LOGIN_WINDOW_NAME);
				// close socket
				if(conn != null) {
					try {
						conn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					conn = null;
				}
				break;
			case CONNECTING:
				// load login window
				if(loginWindow == null) loginWindow = new loginHandler();
				// display connecting card
				if(loginWindow != null) loginWindow.setDisplay(loginFrame.CONNECTING_WINDOW_NAME);
				break;
			case CONNECTED:
				break;
			case AUTHORIZED:
				break;
			}
		}



		// display login/connect window
		loginWindow = new loginHandler();
//login = new frameLogin();
//return;
//		// connect to server
//		conn = new connection("192.168.3.3", 1142);
//		conn.sendPacket(clientPacket.sendHELLO(version, "lorenzo", "pass"));
	}


	public static boolean isStopping() {
		return stopping;
	}


	public static connection getConnectionClass() {
		return conn;
	}
	public static void setConnectionClass(connection conn) {
		if(conn == null) throw new NullPointerException("conn can't be null");
		gcClient.conn = conn;
	}


	public static ConnectState getConnectState() {
		return connectState;
	}
	public static void setConnectState(ConnectState connectState) {
		gcClient.connectState = connectState;
	}


	// load image file/resource
	public static ImageIcon loadImageResource(String path) {
		ImageIcon image = null;
		File file = new File(path);
		if(file.exists())
			image = new ImageIcon(path);
		if(image == null)
			image = new ImageIcon(client.getClass().getResource(path));
		return image;
	}


}
