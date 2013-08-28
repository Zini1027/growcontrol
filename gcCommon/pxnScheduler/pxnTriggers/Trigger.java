package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;


public interface Trigger {

	// set/get value string
	public void setTrigger(String value);
	public String getTriggerOriginal();
	public String getTriggerStr();

	// ms until next trigger
	public long getUntilNext(long time, long last);


}
