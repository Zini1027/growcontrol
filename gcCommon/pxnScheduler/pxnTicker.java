package com.growcontrol.gcCommon.pxnScheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnLogger.pxnLogger;
import com.growcontrol.gcCommon.pxnScheduler.pxnTriggers.triggerInterval;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public class pxnTicker extends pxnSchedulerTask {
	private static final String logName = "gcTicker";
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private static final double tolerance = 20.0;

	// instance
	private static volatile pxnTicker ticker = null;
	private static volatile pxnScheduler sched = null;
	private static final Object lock = new Object();
	private static volatile boolean active = false;
	private static volatile boolean running = false;

	protected final List<pxnTickerTask> tasks = new ArrayList<pxnTickerTask>();
	private final TimeUnitTime interval = new TimeUnitTime(1, TimeU.S);


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
		// add scheduler
		if(sched == null) {
			sched = pxnScheduler.get();
//TODO: add optional threading support
			sched.threadPool.setMax(1);
		}
		sched.newTask(this);
		if(!sched.isAlive())
			sched.start();
	}
	@Override
	public String getTaskName() {
		return logName;
	}


	// start ticker
	public void Start() {
		Stop();
		addTrigger(new triggerInterval(interval));
		getLogger().config("Starting ticker interval: "+interval.getString());
		running = true;
	}
	// stop ticker
	public void Stop() {
		clearTriggers();
		running = false;
	}
	// set tick interval
	public void setInterval(TimeUnitTime interval) {
		this.interval.set(interval);
		if(running)
			Start();
	}


	// pre-run task (return true to cancel)
	@Override
	public boolean preRun(long time) {
		if(super.preRun(time))
			return true;
		synchronized(lock) {
			if(active) {
				getLogger().warning("Ticker still running.. possible lag?");
				return true;
			}
			active = true;
		}
		// has run before
		if(this.lastTriggered != 0) {
			long timeSinceLast = time - this.lastTriggered;
			getLogger().finest("Time since last tick: "+timeSinceLast+"ms");
			double offByPercent = ( ((double)timeSinceLast) / ((double)interval.get(TimeU.MS)) ) * 100.0;
			if(offByPercent < 100.0-tolerance) {
				getLogger().warning("Tick is [ "+Long.toString(interval.get(TimeU.MS)-timeSinceLast)+"ms ] early! possible lag?");
				//getLogger().debug("Time since last tick: "+Long.toString(timeSinceLast)+" off by "+Double.toString(offByPercent)+"%");
			} else
			if(offByPercent > 100.0+tolerance) {
				getLogger().warning("Tick is [ "+Long.toString(timeSinceLast-interval.get(TimeU.MS))+"ms ] late! possible lag?");
				//getLogger().debug("Time since last tick: "+Long.toString(timeSinceLast)+" off by "+Double.toString(offByPercent)+"%");
			}
		}
		return false;
	}
	@Override
	public void run() {
		getLogger().finest("#"+Integer.toString(getRunCount())+" Ticking [ "+Integer.toString(tasks.size())+" ] tasks");
		synchronized(tasks) {
			for(pxnTickerTask task : tasks)
				pxnThreadQueue.addToMain("ticker_"+task.getTaskName(), task);
		}
//		// tick devices
//		gcDeviceLoader.doTick();
//		// tick plugins
//		gcServerPluginLoader.doTick();
		active = false;
	}


	// add task
	public void newTickerTask(pxnTickerTask task) {
		if(task == null) throw new NullPointerException("task cannot be null");
		synchronized(tasks) {
			if(tasks.contains(task))
				return;
			tasks.add(task);
		}
	}
	// remove task
	public void removeTickerTask(pxnTickerTask task) {
		if(task == null) return;
		if(tasks.isEmpty()) return;
		synchronized(tasks) {
			if(tasks.isEmpty())
				return;
			Iterator<pxnTickerTask> it = tasks.iterator();
			while(it.hasNext()) {
				if(task.equals(it.next())) {
					getLogger().debug("Stopping ticker task: "+this.getTaskName());
					it.remove();
					break;
				}
			}
		}
	}


	// logger
	private volatile pxnLogger log = null;
	private synchronized pxnLogger getLogger() {
		if(log == null)
			log = pxnLog.get(logName);
			//log.setBracers("(", ")");
		return log;
	}


}
