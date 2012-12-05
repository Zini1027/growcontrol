package com.growcontrol.gcClient.frames;

public class DashboardHandler {

	protected DashboardFrame frame;


	public DashboardHandler() {
		frame = new DashboardFrame(); 
	}
	public void close() {
		frame.dispose();
		frame = null;
	}


}
