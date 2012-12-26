package com.poixson.pxnListener;

import java.util.ArrayList;
import java.util.List;

import com.poixson.pxnEvent.pxnEvent;
import com.poixson.pxnEvent.pxnEvent.EventPriority;
import com.poixson.pxnLogger.pxnLogger;


public class pxnListenerGroup {

	protected List<pxnListener> listeners = new ArrayList<pxnListener>();


	// register listener
	public void register(pxnListener listener) {
		if(listener == null) throw new NullPointerException("listener cannot be null!");
pxnLogger.getLogger().debug("Registered listener: "+listener.toString());
		synchronized(listeners) {
			listeners.add(listener);
		}
	}


	// trigger event
	public boolean triggerEvent(pxnEvent event, EventPriority onlyPriority) {
		if(event        == null) throw new NullPointerException("event cannot be null!");
		if(onlyPriority == null) throw new NullPointerException("onlyPriority cannot be null!");
		synchronized(listeners) {
			// loop listeners
			for(pxnListener listener : listeners)
				if(listener.priorityEquals(onlyPriority))
					if(listener.doEvent(event))
						event.setHandled();
			return event.isHandled();
		}
	}


}