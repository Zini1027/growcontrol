package com.growcontrol.gcClient.ConnectState;

import com.growcontrol.gcClient.gcClient;
import com.growcontrol.gcClient.frames.DashboardHandler;
import com.growcontrol.gcClient.frames.LoginFrame;
import com.growcontrol.gcClient.frames.LoginHandler;


public class gcConnectState extends ConnectState {

	protected boolean authorized = false;

	// windows
	LoginHandler loginWindow = null;
	DashboardHandler dashboardWindow = null;


	@Override
	protected void doChangedState(State lastState) {
		switch(state) {
		case CLOSED:
			// load login window
			if(loginWindow == null) loginWindow = new LoginHandler();
			if(loginWindow == null)
System.exit(1);
			// display login card
			loginWindow.setDisplay(LoginFrame.LOGIN_WINDOW_NAME);
			// close socket
			if(gcClient.socket != null) {
				gcClient.socket.close();
				gcClient.socket = null;
			}
			break;
		case CONNECTING:
			// load login window
			if(loginWindow == null) loginWindow = new LoginHandler();
			if(loginWindow == null)
System.exit(1);
			// display connecting card
			loginWindow.setDisplay(LoginFrame.CONNECTING_WINDOW_NAME);
			break;
		case CONNECTED:
			// load login window
			if(loginWindow == null) loginWindow = new LoginHandler();
			if(loginWindow == null)
System.exit(1);
			// display connecting card
			loginWindow.setDisplay(LoginFrame.CONNECTING_WINDOW_NAME);
			break;
		case READY:
			if(loginWindow != null) {
				loginWindow.close();
				loginWindow = null;
			}
			// load dashboard window
			if(dashboardWindow == null) dashboardWindow = new DashboardHandler();
			if(dashboardWindow == null)
System.exit(1);
			break;
		}
	}


	// is authorized
	public boolean isAuthorized() {
		return authorized;
	}
	public void setAuthorized(boolean b) {
		this.authorized = b;
	}


	// closed
	public void setStateClosed() {
		setConnectState(State.CLOSED);
	}

	// connecting..
	public void setStateConnecting() {
		setConnectState(State.CONNECTING);
		loginWindow.setMessage("Connecting..");
	}
	public void setStateConnecting(String message) {
		setConnectState(State.CONNECTING);
		loginWindow.setMessage("Failed to connect!");
	}

	// ready!
	public void setStateReady() {
		setConnectState(State.READY);
	}


}
