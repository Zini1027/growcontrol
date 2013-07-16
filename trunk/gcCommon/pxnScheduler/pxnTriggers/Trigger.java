package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import com.growcontrol.gcCommon.TimeUnitTime;


public interface Trigger {

	// set/get value string
	public void setTrigger(String value);
	public String getTriggerOriginal();
	public String getTriggerStr();

	// ms until next trigger
	public TimeUnitTime UntilNext();
	// set timeLast
	public void onTrigger();
	public void onTrigger(long time);


}
