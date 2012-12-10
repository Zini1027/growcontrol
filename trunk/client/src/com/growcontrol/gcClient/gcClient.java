package com.growcontrol.gcClient;

import java.io.File;

import javax.swing.ImageIcon;

import com.growcontrol.gcClient.frames.DashboardHandler;
import com.growcontrol.gcClient.frames.LoginFrame;
import com.growcontrol.gcClient.frames.LoginHandler;
import com.poixson.pxnUtils;
import com.poixson.pxnSocket.pxnSocketClient;


public class gcClient {
	public static final String version = "3.0.3";
	public static gcClient client = null;
	private static boolean stopping = false;
	public static pxnSocketClient socket = null;

	// socket connection state
	public enum ConnectState {CLOSED, CONNECTING, READY};
	private static ConnectState connectState = ConnectState.CLOSED;
	private static ConnectState lastConnectState = null;

	// frame handlers (windows)
	protected LoginHandler loginWindow = null;
	protected DashboardHandler dashboardWindow = null;


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


	// wait for connection state change
	public gcClient() {
		while(!stopping) {
			if(connectState.equals(lastConnectState)) {
				pxnUtils.Sleep(100);
				continue;
			}
			lastConnectState = connectState;
			switch(connectState) {
			case CLOSED:
				// load login window
				if(loginWindow == null) loginWindow = new LoginHandler();
				// display login card
				if(loginWindow != null) loginWindow.setDisplay(LoginFrame.LOGIN_WINDOW_NAME);
				// close socket
				if(socket != null) {
					socket.close();
					socket = null;
				}
				break;
			case CONNECTING:
				// load login window
				if(loginWindow == null) loginWindow = new LoginHandler();
				// display connecting card
				if(loginWindow != null) loginWindow.setDisplay(LoginFrame.CONNECTING_WINDOW_NAME);
				break;
			case READY:
				if(loginWindow != null) {
					loginWindow.close();
					loginWindow = null;
				}
				// load dashboard window
				if(dashboardWindow == null) dashboardWindow = new DashboardHandler();
				break;
			}
		}
//		// connect to server
//		conn = new connection("192.168.3.3", 1142);
//		conn.sendPacket(clientPacket.sendHELLO(version, "lorenzo", "pass"));
	}


	public static boolean isStopping() {
		return stopping;
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
		// open file
		if(file.exists()) {
			try {
				image = new ImageIcon(path);
			} catch(Exception ignore) {}
		}
		// open resource
		if(image == null) {
			try {
				image = new ImageIcon(client.getClass().getResource(path));
			} catch(Exception ignore) {}
		}
		return image;
	}


}
