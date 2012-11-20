package com.growcontrol.gcServer.scheduler;

import com.growcontrol.gcServer.gcServer;

public abstract class gcTickerTask {


	public gcTickerTask() {
		gcServer.getTicker().newTickerTask(this);
	}


	public abstract void tick();


	protected int getTickCount() {
		return gcServer.getTicker().getRunCount();
	}


}
