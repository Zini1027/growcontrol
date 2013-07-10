package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import java.util.concurrent.TimeUnit;

import com.growcontrol.gcCommon.TimeUnitTime;


public interface Trigger {

	// set/get value string
	public void setTrigger(String value);
	public void setTrigger(long value, TimeUnit unit);
	public String getTriggerOriginal();
	public String getTriggerStr();

	// ms until next trigger
	public long UntilNext(TimeUnitTime time);
	public long UntilNext(long timeLast, TimeUnit unit);

}
