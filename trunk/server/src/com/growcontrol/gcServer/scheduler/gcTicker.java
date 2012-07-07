package com.growcontrol.gcServer.scheduler;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.ntp.gcClock;
import com.growcontrol.gcServer.serverPlugin.gcServerPluginLoader;

public class gcTicker implements Runnable {

	private long sessionTicks = 0;
	private double lastTick = 0.0;


	@Override
	public void run() {
		doTick();
	}
	protected synchronized void doTick() {
		double time = gcClock.getTimeMillis();
		if(lastTick!=0.0 && (time-lastTick)/1000.0<0.9) {
			gcServer.log.warning("Tick skipped; possible lag?");
			return;
		}
		lastTick = time;
		sessionTicks++;
		gcServer.log.debug("Tick "+Long.toString(sessionTicks));

		// tick devices
//		gcDeviceLoader.doTick();
		// tick plugins
		gcServerPluginLoader.doTick();
	}


}
