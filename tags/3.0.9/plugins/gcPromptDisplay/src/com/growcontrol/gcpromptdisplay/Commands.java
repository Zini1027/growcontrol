package com.growcontrol.gcpromptdisplay;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandEvent;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;


public final class Commands extends pxnCommandsHolder {

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
		addCommand("promptdisplay")
			.addAlias("prompt")
			.setUsage("");
	}


	@Override
	public boolean onCommand(pxnCommandEvent event) {
return false;
//		if(event.isHandled())   return false;
//		if(!event.hasCommand()) return false;
//		pxnCommand command = event.getCommand();
//gcPromptDisplay.log.severe("gcPromptDisplay Command: "+command.toString());
//		return true;
	}


}
