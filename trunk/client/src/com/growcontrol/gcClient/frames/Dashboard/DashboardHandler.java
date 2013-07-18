package com.growcontrol.gcClient.frames.Dashboard;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.growcontrol.gcClient.frames.gcFrameHandlerInterface;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


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
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					synchronized(frame) {
						frame = new DashboardFrame(handler);
					}
				}
			});
		} catch (InvocationTargetException e) {
			pxnLogger.get().exception(e);
		} catch (InterruptedException ignore) {}
	}
	@Override
	public void Close() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				synchronized(frame) {
					frame.dispose();
					frame = null;
				}
			}
		});
	}
	@Override
	public JFrame getFrame() {
		synchronized(frame) {
			return frame;
		}
	}


}
