package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import com.growcontrol.gcCommon.TimeUnitTime;


public interface Trigger {

	// set/get value string
	public void setTrigger(String value);
//	public void setTrigger(TimeUnitTime value);
//	public void setTrigger(long value, TimeUnit unit);
	public String getTriggerOriginal();
	public String getTriggerStr();

	// ms until next trigger
	public TimeUnitTime UntilNext();
//	public TimeUnitTime UntilNext(TimeUnitTime time);
//	public long UntilNext(long timeLast, TimeUnit unit);
	// set timeLast
	public void onTrigger();
	public void onTrigger(long time);


}
