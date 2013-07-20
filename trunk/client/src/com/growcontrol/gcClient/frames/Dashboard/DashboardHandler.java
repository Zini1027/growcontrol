package com.growcontrol.gcClient.frames.Dashboard;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.growcontrol.gcClient.frames.gcFrameHandlerInterface;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;


public class DashboardHandler implements gcFrameHandlerInterface {
	protected static DashboardHandler handler = null;

	// display mode
	public enum DASH {ROOM, TREE, DIAGRAM};
	protected volatile DASH dashMode     = null;
	protected volatile DASH lastDashMode = null;
	private final Object modeLock = new Object();

	// objects
	protected DashboardFrame frame = null;


	// dash frame handler
	public static synchronized DashboardHandler get() {
		if(handler == null)
			handler = new DashboardHandler();
		return handler;
	}
	public DashboardHandler() {
	}


	@Override
	public void Show() {
		synchronized(modeLock) {
			if(dashMode == null)
				dashMode = DASH.ROOM;
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						if(frame == null)
							frame = new DashboardFrame(handler);
					}
				});
			} catch (InvocationTargetException e) {
				pxnLogger.get().exception(e);
			} catch (InterruptedException ignore) {}
		}
	}
	@Override
	public void Close() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(frame == null)
					return;
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
