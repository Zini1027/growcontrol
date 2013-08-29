package com.growcontrol.gcCommon;

import java.util.concurrent.TimeUnit;


public final class TimeU {
	private TimeU() {}
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public static final TimeUnit MS = TimeUnit.MILLISECONDS;
	public static final TimeUnit S  = TimeUnit.SECONDS;
	public static final TimeUnit M  = TimeUnit.MINUTES;
	public static final TimeUnit H  = TimeUnit.HOURS;
	public static final TimeUnit D  = TimeUnit.DAYS;

}
