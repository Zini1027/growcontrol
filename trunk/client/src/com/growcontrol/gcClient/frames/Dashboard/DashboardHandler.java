package com.growcontrol.gcClient.frames.Dashboard;

import javax.swing.JFrame;

import com.growcontrol.gcClient.frames.gcFrameHandlerInterface;


public class DashboardHandler implements gcFrameHandlerInterface {

	protected static DashboardHandler handler = null;
	protected DashboardFrame frame = null;


	// dash frame handler
	public static DashboardHandler get() {
		synchronized(handler) {
			if(handler == null)
				handler = new DashboardHandler();
		}
		return handler;
	}
	public DashboardHandler() {
	}


	@Override
	public void Show() {
		frame = new DashboardFrame(this);
	}
	@Override
	public void Close() {
		frame.dispose();
		frame = null;
	}
	@Override
	public JFrame getFrame() {
		return frame;
	}


}
