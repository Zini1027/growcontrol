package com.growcontrol.gcClient.frames;

import javax.swing.JFrame;


public class DashboardHandler implements gcFrameHandler {

	protected DashboardFrame frame;


	public DashboardHandler() {
		frame = new DashboardFrame(); 
	}
	@Override
	public void close() {
		frame.dispose();
		frame = null;
	}
	@Override
	public JFrame getFrame() {
		return frame;
	}


}
