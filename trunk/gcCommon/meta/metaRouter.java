package com.growcontrol.gcCommon.meta;

import java.util.HashMap;
import java.util.Map.Entry;

import com.growcontrol.gcCommon.pxnLogger.pxnLog;
import com.growcontrol.gcCommon.pxnThreadQueue.pxnThreadQueue;


public final class metaRouter {
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private static volatile metaRouter router = null;
	private static final Object lock = new Object();

	// input listeners
	private HashMap<String, valueReceiver> listeners = new HashMap<String, valueReceiver>();

	// logic thread pool
	private final pxnThreadQueue threads;


	// router instance
	public static metaRouter get() {
		if(router == null) {
			synchronized(lock) {
				if(router == null)
					router = new metaRouter();
			}
		}
		return router;
	}
	private metaRouter() {
		// logic thread queue (default to main thread)
		threads = new pxnThreadQueue("MetaRouter", 0);
	}


	// max logic threads
	public void setMaxThreads(int maxThreads) {
		threads.setMax(maxThreads);
	}


	// meta input
	public void register(valueReceiver listener) {
		if(listener == null) throw new NullPointerException("listener cannot be null!");
		String name = listener.getName();
		// check for existing listener
		synchronized(listeners) {
			if(listeners.containsKey(name)) {
				String newName = null;
				int i = 1;
				while(true) {
					i++;
					newName = name+"-"+Integer.toString(i);
					if(!listeners.containsKey(newName))
						break;
				}
				pxnLog.get(name).warning("Input listener already exists with this name. Renaming to: "+newName);
				name = newName;
			}
			// add listener (with unique name)
			listeners.put(name, listener);
		}
	}


	// meta output
	public boolean Send(String toName, metaValue meta) {
		synchronized(listeners) {
			for(Entry<String, valueReceiver> entry : listeners.entrySet()) {
				String name = entry.getKey();
				if(name == null || name.isEmpty()) continue;
				if(entry.getValue() == null) continue;
				// name matches
				if(!name.equalsIgnoreCase(toName)) continue;
				// send to meta input listener
				valueReceiver listener = entry.getValue();
				if(listener.onProcess(meta))
					meta.handled(true);
			}
		}
		return meta.handled();
	}


}
