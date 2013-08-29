package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;


// cron trigger
// example:
public class triggerCron implements Trigger {

	protected volatile String rawValue = null;
	protected volatile long last = 0;


	public triggerCron(String value) {
		setTrigger(value);
	}


	// set/get value string
	@Override
	public void setTrigger(String value) {
		if(value == null) return;
		this.rawValue = value;
	}
	@Override
	public String getTriggerOriginal() {
		return this.rawValue;
	}
	@Override
	public String getTriggerStr() {
//TODO: generate new string
		return this.rawValue;
	}


	// time until next trigger
	@Override
	public long getUntilNext(long time, long last) {
		return Long.MIN_VALUE;
	}


}
