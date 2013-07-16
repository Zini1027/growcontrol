package com.growcontrol.gcServer.scheduler;


public abstract class gcTickerTask {


//	public gcTickerTask() {
//		gcServer.get().getTicker().newTickerTask(this);
//	}


	public abstract void tick();


//	protected int getTickCount() {
//		return gcServer.get().getTicker().getRunCount();
//	}


}
