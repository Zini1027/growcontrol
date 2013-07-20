package com.growcontrol.gcClient;

import com.growcontrol.gcClient.frames.Dashboard.DashboardHandler;
import com.growcontrol.gcClient.frames.Login.LoginHandler;


public class guiManager {
	public static guiManager manager = null;

	// display mode
	public enum GUI {LOGIN, DASH};
	private static volatile GUI guiMode     = null;
	private static volatile GUI lastGuiMode = null;

	// frames
	protected LoginHandler     loginHandler = null;
	protected DashboardHandler dashHandler  = null;


	public static synchronized guiManager get() {
		if(manager == null)
			manager = new guiManager();
		return manager;
	}
	private guiManager() {
		doUpdate();
	}


	public void doUpdate() {
		getMode();
		synchronized(guiMode) {
			// update gui mode
			if(guiMode != lastGuiMode) {
				switch(guiMode) {
				case LOGIN:
					getLoginHandler().Show();
					break;
				case DASH:
					getDashboardHandler().Show();
					break;
				}
				lastGuiMode = guiMode;
			}
		}
	}


	public synchronized LoginHandler getLoginHandler() {
		if(loginHandler == null)
			loginHandler = new LoginHandler();
		return loginHandler;
	}
	public synchronized DashboardHandler getDashboardHandler() {
		if(dashHandler == null)
			dashHandler = new DashboardHandler();
		return dashHandler;
	}


	// get/set gui mode
	public GUI getMode() {
		if(guiMode == null)
			guiMode = GUI.LOGIN;
		return guiMode;
	}
	public GUI setMode(GUI mode) {
		synchronized(guiMode) {
			lastGuiMode = guiMode;
			guiMode = mode;
			return lastGuiMode;
		}
	}


	// gui mode to string
	public static String modeToString(GUI mode) {
		switch(mode) {
		case LOGIN:
			return "LOGIN";
		case DASH:
			return "DASH";
		default:
		}
		return "UNKNOWN";
	}


}
