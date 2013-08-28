package com.growcontrol.gcCommon.pxnScheduler;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcCommon.pxnScheduler.pxnTriggers.Trigger;


public abstract class pxnSchedulerTask implements Runnable {

	protected List<Trigger> triggers = new ArrayList<Trigger>();
	protected volatile long lastTriggered = Long.MIN_VALUE;
	protected volatile boolean multiThreaded;
	protected volatile boolean paused = false;
	private volatile int sleepCycles = 0;
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
	// task name
	public abstract String getTaskName();


	public pxnSchedulerTask addTrigger(Trigger trigger) {
		if(trigger == null) return null;
		synchronized(triggers) {
			if(!triggers.contains(trigger))
				triggers.add(trigger);
		}
		return this;
	}
	public void clearTriggers() {
		synchronized(triggers) { 
			triggers.clear();
		}
	}


	public long UntilNextTrigger(long time) {
		if(paused || time < 1)
			return Long.MIN_VALUE;
		long untilNext = Long.MIN_VALUE;
		synchronized(triggers) {
			if(this.lastTriggered == Long.MIN_VALUE)
				this.lastTriggered = time;
			for(Trigger trigger : triggers) {
				long u = trigger.getUntilNext(time, this.lastTriggered);
				if(u == Long.MIN_VALUE) continue;
				if(u < untilNext || untilNext == Long.MIN_VALUE)
					untilNext = u;
			}
		}
		return untilNext;
	}


	// pre-run task (return true to cancel)
	public boolean preRun(long time) {
		runCount++;
		return false;
	}
	public void doingTrigger(long time) {
		if(paused) return;
		// run count
		if(maxRunCount != -1 && runCount >= maxRunCount) {
			paused = true;
			return;
		}
		this.lastTriggered = time;
	}


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
//	public void Stop() {
//		paused = true;
//	}


	// sleep task
	public int SleepCycles() {
		sleepCycles--;
		return sleepCycles;
	}
	public void SleepCycles(int cycles) {
		sleepCycles = cycles;
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


}
