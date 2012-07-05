package com.growcontrol.gcServer.scheduler;

import com.growcontrol.gcServer.gcServer;

public class gcTicker implements Runnable {


	@Override
	public void run() {
		gcServer.log.severe("TICK");
	}


}
