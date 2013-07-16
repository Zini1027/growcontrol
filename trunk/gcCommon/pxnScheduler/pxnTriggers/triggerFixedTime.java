package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import java.util.Calendar;

import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnClock.pxnClock;


// fixed time trigger
// example:
public class triggerFixedTime implements Trigger {

	protected String rawValue;
	protected Calendar cal;
	protected volatile long timeLast = 0;


	public triggerFixedTime(String value) {
		if(value == null) value = "";
		this.rawValue = value.trim().toLowerCase();
		
	}


	// set/get value string
	@Override
	public void setTrigger(String value) {
		this.rawValue = value;
//TODO: parse calendar value
	}
	public void setTrigger(Calendar cal) {
		this.cal = cal;
		this.rawValue = this.getTriggerStr();
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
