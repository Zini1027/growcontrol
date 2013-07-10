package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import java.util.concurrent.TimeUnit;

import com.growcontrol.gcCommon.TimeUnitTime;


// cron trigger
// example:
public class triggerCron implements Trigger {

	protected String rawValue;


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
	public void setTrigger(long value, TimeUnit unit) {
//		this.value = TimeUnit.MILLISECONDS.convert(value, unit);
//		this.rawValue = this.getTriggerStr();
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
	public long UntilNext(TimeUnitTime time) {
		return UntilNext(time.get(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
	}
	@Override
	public long UntilNext(long timeLast, TimeUnit unit) {
		return 0;
	}


}
