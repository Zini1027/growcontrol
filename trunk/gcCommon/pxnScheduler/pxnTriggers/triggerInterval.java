package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.TimeUnitTime;
import com.growcontrol.gcCommon.pxnClock.pxnClock;


// interval trigger
// example: "5s 3h 10d"
public class triggerInterval implements Trigger {

	protected String rawValue;
	protected TimeUnitTime interval = new TimeUnitTime();
	protected volatile long timeLast = 0;


	public triggerInterval(String value) {
		setTrigger(value);
	}


	// set/get value string
	@Override
	public void setTrigger(String interval) {
		if(interval == null) interval = "";
		this.rawValue = interval.trim().toLowerCase();
		this.interval.set(
			TimeUnitTime.ParseDuration(this.rawValue)
		);
	}
	public void setTrigger(TimeUnitTime interval) {
		if(interval == null) throw new NullPointerException("interval cannot be null!");
		this.interval.set(interval);
		this.rawValue = this.getTriggerStr();
	}
	@Override
	public String getTriggerOriginal() {
		return rawValue;
	}
	@Override
	public String getTriggerStr() {
//TODO: generate new string
		return rawValue;
	}


	// time until next trigger
	@Override
	public TimeUnitTime UntilNext() {
		long timeNow = getTime();
		if(timeLast == 0) timeLast = timeNow;
		long timeSinceLast = timeNow - timeLast;
//System.out.println("timeNow:"+timeNow+" timeLast:"+timeLast+" timeSinceLast:"+timeSinceLast+" timeUntil:"+(interval.get(TimeU.MS) - timeSinceLast) );
		return new TimeUnitTime(
			(interval.get(TimeU.MS) - timeSinceLast),
			TimeU.MS);
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
