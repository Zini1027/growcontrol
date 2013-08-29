package com.growcontrol.gcCommon.pxnScheduler.pxnTriggers;

import com.growcontrol.gcCommon.TimeU;
import com.growcontrol.gcCommon.TimeUnitTime;


// interval trigger
// example: "5s 3h 10d"
public class triggerInterval implements Trigger {

	protected volatile String rawValue = null;
	protected final TimeUnitTime interval = new TimeUnitTime();


	public triggerInterval(String value) {
		setTrigger(value);
	}
	public triggerInterval(TimeUnitTime value) {
		setTrigger(value);
	}


	// set/get value string
	@Override
	public void setTrigger(String interval) {
		if(interval == null) return;
		this.rawValue = interval.trim().toLowerCase();
		this.interval.set(
			TimeUnitTime.Parse(this.rawValue)
		);
	}
	public void setTrigger(TimeUnitTime interval) {
		if(interval == null) return;
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
	public long getUntilNext(long time, long last) {
		if(last < 1)
			last = time;
		long timeSinceLast = time - last;
		long untilNext = interval.get(TimeU.MS) - timeSinceLast;
//pxnLog.get().severe("timeNow:"+timeNow+" timeLast:"+timeLast+" timeSinceLast:"+timeSinceLast+" timeUntil:"+(interval.get(TimeU.MS) - timeSinceLast) );
		return untilNext;
	}


}
