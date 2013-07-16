package com.growcontrol.gcCommon.pxnThreadQueue;


public class pxnRunnable {

	protected final String taskName;
	protected final Runnable runnable;


	public pxnRunnable(Runnable run) {
		this(null, run);
	}
	public pxnRunnable(String taskName, Runnable run) {
		if(run == null) throw new NullPointerException("runnable cannot be null!");
		if(taskName == null || taskName.equals("")) taskName = null;
		this.runnable = run;
		this.taskName = taskName;
	}


	public void run() {
		if(this.runnable != null)
			this.runnable.run();
	}
	public String getTaskName() {
		return this.taskName;
	}


}
