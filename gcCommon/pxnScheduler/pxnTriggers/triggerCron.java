package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnClock.pxnClock;


// cron trigger
// example:
public class triggerCron implements Trigger {

	protected String rawValue;
	protected volatile long timeLast = 0;


	public triggerCron(String value) {
		if(value == null) value = "";
		this.rawValue = value.trim().toLowerCase();
		
	}


	// set/get value string
	@Override
	public void setTrigger(String value) {
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
	public TimeUnitTime UntilNext() {
		return null;
	}
	@Override
	public void onTrigger() {
		timeLast = getTime();
	}
	@Override
	public void onTrigger(long time) {
		timeLast = time;
	}


	protected static long getTime() {
		return pxnClock.get().Millis();
	}


}
