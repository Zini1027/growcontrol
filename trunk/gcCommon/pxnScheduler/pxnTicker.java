package com.growcontrol.gcCommon.pxnScheduler;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnScheduler.pxnTriggers.triggerInterval;


public class pxnTicker extends pxnSchedulerTask {
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	// instance
	private static volatile pxnTicker ticker = null;
	private static final Object lock = new Object();

	protected List<pxnTickerTask> tasks = new ArrayList<pxnTickerTask>();

	private TimeUnitTime interval = new TimeUnitTime(1, TimeU.S);


	public static pxnTicker get() {
		if(ticker == null) {
			synchronized(lock) {
				if(ticker == null)
					ticker = new pxnTicker();
			}
		}
		return ticker;
	}
	protected pxnTicker() {
		// new task (multi-threaded / repeat)
		super(true, true);
//		long tickInterval = ServerConfig.get().TickInterval();
//		setInterval(new TimeUnitTime(tickInterval, TimeU.MS));
		// add scheduler
		pxnScheduler.get("ticker").newTask(this);
	}
	@Override
	public String getTaskName() {
		return "gcTicker";
	}


	public void setInterval(TimeUnitTime interval) {
		this.interval.set(interval);
		clearTriggers();
		addTrigger(new triggerInterval(Long.toString(interval.get(TimeU.MS))+"n"));
	}


	@Override
	public void preRun() {
		if(timeLast == 0) return;
		long time = getTime();
		long timeSinceLast = time - timeLast;
		double offByPercent = ((double)interval.get(TimeU.MS)) / ((double)timeSinceLast);
		if(offByPercent < 0.9 || offByPercent > 1.1) {
pxnLog.get().warning("Tick to soon! possible lag?");
pxnLog.get().debug("Time since last tick: "+Long.toString(timeSinceLast));
		return;
	}
//	} else
//	if( ((double)tickInterval) / ((double)timeSinceLast) > 1.1) {
//		Main.getLogger().warning("Tick skipped; possible lag?");
//Main.getLogger().debug("Time since last tick: "+Long.toString(timeSinceLast));
//		return;
	}
	@Override
	public void run() {
System.out.println("TICK run()");
//		Main.getLogger().debug("Tick "+Integer.toString(getRunCount()));
//		synchronized(tasks) {
//			for(gcTickerTask task : tasks) {
//				task.tick();
//			}
//		}
//		// tick devices
//		gcDeviceLoader.doTick();
//		// tick plugins
//		gcServerPluginLoader.doTick();
	}


	public void newTickerTask(pxnTickerTask task) {
		if(task == null) throw new NullPointerException("task cannot be null");
		synchronized(tasks) {
			tasks.add(task);
		}
	}


}
