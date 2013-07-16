package com.growcontrol.gcCommon.pxnScheduler;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnClock.pxnClock;
import com.growcontrol.gcCommon.pxnScheduler.pxnTriggers.Trigger;


public abstract class pxnSchedulerTask implements Runnable {

	protected List<Trigger> triggers = new ArrayList<Trigger>();
	protected volatile boolean multiThreaded;
	protected volatile boolean paused = false;
	protected volatile long timeLast = 0;
	public volatile int isSleeping = 0;
	// run count
	protected final boolean repeat;
	protected final int maxRunCount;
	private volatile int runCount = 0;

//	private volatile boolean active = false;
//	private volatile long execTime = 0;


	// new task (multi-threaded, repeating)
	public pxnSchedulerTask(boolean multiThreaded, boolean repeat) {
		this.multiThreaded = multiThreaded;
		this.repeat = repeat;
		this.maxRunCount = -1;
		init();
	}
	// new task (multi-threaded, repeat count)
	public pxnSchedulerTask(boolean multiThreaded, int maxRunCount) {
		this.multiThreaded = multiThreaded;
		this.repeat = true;
		if(maxRunCount < 0) maxRunCount = -1;
		this.maxRunCount = maxRunCount;
		init();
	}
	// init task
	protected void init() {
	}


	public pxnSchedulerTask addTrigger(Trigger trigger) {
		if(trigger == null) return null;
		this.triggers.add(trigger);
		return this;
	}
	public void clearTriggers() {
		this.triggers.clear();
	}


	public TimeUnitTime UntilNextTrigger() {
		if(paused) return null;
		TimeUnitTime untilNext = null;
		synchronized(this.triggers) {
			for(Trigger trigger : this.triggers) {
				TimeUnitTime u = trigger.UntilNext();
				if(untilNext == null) {
					untilNext = u;
					continue;
				}
				if(u.get(TimeU.MS) < untilNext.get(TimeU.MS))
					untilNext.set(u);
			}
		}
		return untilNext;
	}


	// run task
	public void preRun() {}
	public void onTrigger() {
		if(paused) return;
		// run count
		if(maxRunCount != -1 && runCount >= maxRunCount) {
			paused = true;
			return;
		}
		// set timeLast
		long time = getTime();
		this.timeLast = time;
		synchronized(this.triggers) {
			for(Trigger trigger : this.triggers) {
				TimeUnitTime untilNext = trigger.UntilNext();
				if(untilNext.get(TimeU.MS) <= 0)
					trigger.onTrigger(time);
			}
		}
		runCount++;
	}


	// task name
	public abstract String getTaskName();


	// single/multi-threaded
	public boolean isMultiThreaded() {
		return multiThreaded;
	}
	// stop / pause
	public boolean isPaused() {
		return paused;
	}
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	public void stop() {
		paused = true;
	}


	// stats
	public int getRunCount() {
		return runCount;
	}
	public long getAverageExecTime() {
//		if(runCount < 1) return -1;
//		return execTime / ((long) runCount);
		return -1;
	}


	protected static long getTime() {
		return pxnClock.get().Millis();
	}


}
