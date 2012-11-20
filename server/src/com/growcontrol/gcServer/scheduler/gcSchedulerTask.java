package com.growcontrol.gcServer.scheduler;

import java.util.Timer;
import java.util.TimerTask;



public abstract class gcSchedulerTask {

	// multi-threaded
	protected final boolean multiThreaded;
	protected Timer timer = null;

	private boolean paused = false;
	protected final boolean repeat;
	private int runCount = 0;
	private int maxRunCount = 0;
	private long execTime = 0;


	public gcSchedulerTask(boolean multiThreaded) {
		this(multiThreaded, true);
	}
	public gcSchedulerTask(boolean multiThreaded, boolean repeat) {
		this.multiThreaded = multiThreaded;
		this.repeat = repeat;
		start();
	}
	public gcSchedulerTask(boolean multiThreaded, int maxRunCount) {
		this.multiThreaded = multiThreaded;
		this.maxRunCount = maxRunCount;
		this.repeat = true;
		start();
	}


	// start scheduler task
	private void start() {
		if(multiThreaded) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					doTrigger();
				}
			}, 1000L, 1000L);
		}
	}


	// trigger tick
	public abstract void trigger();
	public void doTrigger() {
		if(paused) return;
		// run count
		runCount++;
		if(checkMaxRunCount()) return;
//gcServer.log.printRaw("Run Count: "+Integer.toString(runCount));
		trigger();
	}
//	public void triggerThread() {
//		if(thread == null && multiThreaded) start();
//		if(thread == null) throw new NullPointerException();
//		thread.start();
//	}
	public boolean checkMaxRunCount() {
		if(maxRunCount >= runCount) {
			paused = true;
			return true;
		}
		return false;
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
		if(runCount < 1) return -1;
		return execTime / ((long) runCount);
	}


}
