package com.growcontrol.example;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandEvent;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;
import com.growcontrol.gcCommon.pxnLogger.pxnLog;


public class Commands extends pxnCommandsHolder {


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
