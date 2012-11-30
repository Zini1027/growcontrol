package com.growcontrol.gcServer.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcServer.gcServer;
import com.poixson.pxnUtils;


public class gcTicker extends gcSchedulerTask {

	protected String taskName = "gcTicker";

	private long tickInterval = 0;
	private long timeLast     = 0;

	protected List<gcTickerTask> tasks = new ArrayList<gcTickerTask>();


	public gcTicker() {
		super(true, true); // set multi-threaded / repeat
		tickInterval = gcServer.config.getTickInterval();
		gcServer.getScheduler().newTask(this);
//		gcScheduler.newTriggerSeconds(1, true);
	}


	@Override
	public String getTaskName() {
		return "Ticker";
	}


	@Override
	public void trigger() {
		long time = pxnUtils.getClock().getTimeMillis();
		long timeSinceLast = 0;
		if(timeLast != 0) {
			timeSinceLast = time - timeLast;
			if( ((double)tickInterval) / ((double)timeSinceLast) < 0.9) {
				gcServer.log.warning("Tick to soon! possible lag?");
gcServer.log.debug("Time since last tick: "+Long.toString(timeSinceLast));
				return;
			} else
			if( ((double)tickInterval) / ((double)timeSinceLast) > 1.1) {
				gcServer.log.warning("Tick skipped; possible lag?");
gcServer.log.debug("Time since last tick: "+Long.toString(timeSinceLast));
				return;
			}
		}
		// trigger tick
		timeLast = time;
//		super.run();
		gcServer.log.debug("Tick "+Integer.toString(getRunCount()));
		synchronized(tasks) {
			for(gcTickerTask task : tasks) {
				task.tick();
			}
		}
	}
//	// tick devices
//	gcDeviceLoader.doTick();
//	// tick plugins
//	gcServerPluginLoader.doTick();


	public void newTickerTask(gcTickerTask task) {
		if(task == null) throw new NullPointerException("task cannot be null");
		synchronized(tasks) {
			tasks.add(task);
		}
	}


}
