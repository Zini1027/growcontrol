package com.growcontrol.gcServer.scheduler;

public class gcSchedulerTask implements Runnable {

//	protected Runnable runnableTask = null;

	protected String taskName = null;
	private boolean paused = false;
	private int runCount = 0;
	private long execTime = 0;


	public gcSchedulerTask() {
	}


	public void run() {
		runCount++;
System.out.println("Run Count: "+Integer.toString(runCount));
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


	// name
	public String getTaskName() {
		return taskName;
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
