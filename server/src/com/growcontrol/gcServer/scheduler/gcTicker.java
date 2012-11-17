package com.growcontrol.gcServer.scheduler;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.ntp.gcClock;

public class gcTicker extends gcSchedulerTask {

	protected String taskName = "gcTicker";

	private long sessionTicks = 0;
	private double lastTick = 0.0;


//	public gcTicker() {
//		super();
//		gcScheduler.newTriggerSeconds(1, true);
//	}


	@Override
	public void run() {
		triggerTick();
	}
	protected synchronized void triggerTick() {
		double time = gcClock.getTimeMillis();
System.out.println(time);
		if(lastTick!=0.0 && (time-lastTick)/1000.0<0.9) {
			gcServer.log.warning("Tick skipped; possible lag?");
			return;
		}
		lastTick = time;
		sessionTicks++;
//		gcServer.log.debug("Tick "+Long.toString(sessionTicks));

		// tick devices
//		gcDeviceLoader.doTick();
		// tick plugins
//		gcServerPluginLoader.doTick();
	}


}
