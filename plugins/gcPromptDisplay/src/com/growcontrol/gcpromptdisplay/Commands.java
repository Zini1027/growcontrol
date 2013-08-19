package com.growcontrol.gcpromptdisplay;

import com.growcontrol.gcCommon.pxnCommand.pxnCommandEvent;
import com.growcontrol.gcCommon.pxnCommand.pxnCommandsHolder;


public final class Commands extends pxnCommandsHolder {


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
