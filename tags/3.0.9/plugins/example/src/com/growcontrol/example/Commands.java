package com.growcontrol.example;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandEvent;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public class Commands extends pxnCommandsHolder {

	private static volatile Commands instance = null;
	private static final Object lock = new Object();


	public static Commands get() {
		if(instance == null) {
			synchronized(lock) {
				if(instance == null)
					instance = new Commands();
			}
		}
		return instance;
	}
	@Override
	protected void initCommands() {
//		setPriority(EventPriority.NORMAL);
		// register commands
		addCommand("example")
			.addAlias("exm")
			.setUsage("This is an example command.");
	}


	@Override
	public boolean onCommand(pxnCommandEvent event) {
		pxnLog.get().Publish("Example command has run");
		return true;
	}


}
