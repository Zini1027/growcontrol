package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import java.util.concurrent.TimeUnit;

import com.growcontrol.gcCommon.TimeUnitTime;


// interval trigger
// example: "5s 3h 10d"
public class triggerInterval implements Trigger {

	protected String rawValue;
	protected long value; // ms interval


	public triggerInterval(String value) {
		setTrigger(value);
	}


	// set/get value string
	@Override
	public void setTrigger(String value) {
		if(value == null) value = "";
		this.rawValue = value.trim().toLowerCase();
		this.value = TimeUnitTime.ParseDuration(this.rawValue, TimeUnit.MILLISECONDS);
	}
	@Override
	public void setTrigger(long value, TimeUnit unit) {
		if(unit == null) throw new NullPointerException("unit cannot be null!");
		this.value = TimeUnit.MILLISECONDS.convert(value, unit);
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
	public long UntilNext(TimeUnitTime time) {
		return UntilNext(time.get(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
	}
	@Override
	public long UntilNext(long timeLast, TimeUnit unit) {
		return unit.convert(timeLast % this.value, TimeUnit.MILLISECONDS);
	}


}
