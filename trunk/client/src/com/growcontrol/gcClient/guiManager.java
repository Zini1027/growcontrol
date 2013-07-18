package com.growcontrol.gcClient;

import com.growcontrol.gcClient.frames.Dashboard.DashboardHandler;
import com.growcontrol.gcClient.frames.Login.LoginHandler;


public class guiManager {

	public static guiManager manager = null;

	// display mode
	public static class DisplayMode {

		public enum ModeGui {LOGIN, DASH};
		public enum ModeDash {ROOM, TREE, DIAGRAM};

		public ModeGui mode          = ModeGui.LOGIN;
		public ModeGui lastMode      = null;
		public ModeDash modeDash     = ModeDash.ROOM;
		public ModeDash lastModeDash = null;

	}

	// frames
	protected LoginHandler loginHandler = null;
	protected DashboardHandler dashHandler = null;


	public guiManager get() {
		synchronized(manager) {
			if(manager == null)
				manager = new guiManager();
		}
		return manager;
	}
	private guiManager() {
		doUpdate();
	}


	public void doUpdate() {
	}


	public LoginHandler getLoginHandler() {
		return loginHandler;
	}
	public DashboardHandler getDashboardHandler() {
		return dashHandler;
	}


}
