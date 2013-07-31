package com.growcontrol.gcCommon;

import java.util.concurrent.TimeUnit;


public final class TimeU {
	private TimeU() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public final static TimeUnit MS = TimeUnit.MILLISECONDS;
	public final static TimeUnit S  = TimeUnit.SECONDS;
	public final static TimeUnit M  = TimeUnit.MINUTES;
	public final static TimeUnit H  = TimeUnit.HOURS;
	public final static TimeUnit D  = TimeUnit.DAYS;

}
