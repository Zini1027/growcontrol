package org.slf4j.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class slf4jCustomFactory implements ILoggerFactory {

	private Map<String, slf4jCustomAdapter> loggerMap;

	public slf4jCustomFactory() {
		loggerMap = new HashMap<String, slf4jCustomAdapter>();
	}

	@Override
	public Logger getLogger(String name) {
		synchronized (loggerMap) {
			if(!loggerMap.containsKey(name))
				loggerMap.put(name, new slf4jCustomAdapter());
			return loggerMap.get(name);
		}
	}

}
