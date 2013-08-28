package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import java.util.Calendar;


// fixed time trigger
// example:
public class triggerFixedTime implements Trigger {

	protected volatile String rawValue = null;;
	protected volatile Calendar cal;
	protected volatile long last = 0;


	public triggerFixedTime(String value) {
		setTrigger(value);
//TODO:
		cal = null;
	}


	// set/get value string
	@Override
	public void setTrigger(String value) {
		if(value == null) return;
		this.rawValue = value.trim().toLowerCase();
//TODO: parse calendar value
	}
	public void setTrigger(Calendar cal) {
		if(cal == null) return;
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
	public long getUntilNext(long time, long last) {
		return Long.MIN_VALUE;
	}


}
