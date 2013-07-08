package com.growcontrol.gcCommon.pxnCommand;

import com.growcontrol.gcCommon.pxnEvent.pxnEvent.EventPriority;
import com.growcontrol.gcCommon.pxnListener.pxnListener;
import com.growcontrol.gcCommon.pxnListener.pxnListenerGroup;


public class pxnCommandListenerGroup extends pxnListenerGroup {


	// trigger event
	public boolean triggerCommandEvent(String line) {
		if(line == null) throw new NullPointerException("line cannot be null!");
		boolean result = false;
		if(triggerCommandEventPriority(line, EventPriority.HIGHEST)) result = true;
		if(triggerCommandEventPriority(line, EventPriority.HIGH   )) result = true;
		if(triggerCommandEventPriority(line, EventPriority.NORMAL )) result = true;
		if(triggerCommandEventPriority(line, EventPriority.LOW    )) result = true;
		if(triggerCommandEventPriority(line, EventPriority.LOWEST )) result = true;
		return result;
	}
	// trigger listeners by priority
	protected boolean triggerCommandEventPriority(String line, EventPriority onlyPriority) {
		if(line         == null) throw new NullPointerException("line cannot be null!");
		if(onlyPriority == null) throw new NullPointerException("onlyPriority cannot be null!");
		boolean result = false;
		synchronized(listeners) {
			// loop listeners
			for(pxnListener listener : listeners) {
				if(!(listener instanceof pxnCommandsHolder)) continue;
				if(!listener.priorityEquals(onlyPriority)) continue;
				pxnCommandsHolder commandListener = (pxnCommandsHolder) listener;
				// new event
				pxnCommandEvent event = pxnCommandEvent.newEvent(line, commandListener);
				// command/alias not found
				if(event == null) continue;
				// run event
				if(result) event.setHandled();
				if(commandListener.onCommand(event))
					event.setHandled();
				if(event.isHandled()) result = true;
			}
		}
		return result;
	}


}
