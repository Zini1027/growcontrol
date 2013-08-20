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
	private HashMap<String, metaInput> listeners = new HashMap<String, metaInput>();

	// logic thread pool
	private final pxnThreadQueue threads;


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
		// logic thread queue
		threads = new pxnThreadQueue("MetaRouter");
		// default logic to main thread
		threads.setMax(0);
	}


	// max logic threads
	public void setMaxThreads(int maxThreads) {
		threads.setMax(maxThreads);
	}


	// meta input
	public void register(metaInput listener) {
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
	public boolean Send(String toName, pxnMetaType meta) {
		synchronized(listeners) {
			for(Entry<String, metaInput> entry : listeners.entrySet()) {
				String name = entry.getKey();
				if(name == null || name.isEmpty()) continue;
				if(entry.getValue() == null) continue;
				// name matches
				if(!name.equalsIgnoreCase(toName)) continue;
				// send to meta input listener
				metaInput listener = entry.getValue();
				if(listener.onInput(meta))
					meta.setHandled();
			}
		}
		return meta.isHandled();
	}


}
