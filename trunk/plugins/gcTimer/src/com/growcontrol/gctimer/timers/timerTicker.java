package com.growcontrol.gctimer.timers;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.devices.gcServerDeviceBoolean;
import com.growcontrol.gctimer.gcTimer;
import com.growcontrol.gctimer.gcTimer.TimerType;

public class timerTicker extends gcServerDeviceBoolean implements deviceTimer {

	private String title = "";
	private long duration = 0;
	private long currentTick = 0;

	private List<Span> spans = new ArrayList<Span>();


	// ticker span class
	public class Span {
		public long onTick = -1;
		public long offTick = -1;
		public Span(long onTick, long offTick, long duration) {
			this.onTick  = onTick;
			this.offTick = offTick;
			validate(duration);
		}
		public void validate(long duration) {
			onTick  = gcServer.MinMax(onTick,  0, duration-1);
			offTick = gcServer.MinMax(offTick, 0, duration-1);
		}
	}


	public timerTicker(String name, String title) {
		super(name);
		if(title==null || title.isEmpty())
			this.title = name;
		else
			this.title = title;
	}
	@Override
	public TimerType getTimerType() {
		return TimerType.TICKER;
	}


	public void StartDevice(RunMode runMode) {
		gcTimer.log.info("Starting timer: "+title+" [Ticker | cycle | "+Long.toString(duration)+"]");
		super.StartDevice(runMode);
	}


	// set duration
	public void setDuration(String durationStr) {
		if(durationStr.endsWith("s"))
			setDuration(durationStr.substring(0, durationStr.length()));
		long duration = 0;
		try {
			duration = Long.valueOf(durationStr);
		} catch(Exception ignore) {}
		this.duration = duration;
	}


	// add ticker span
	public void addSpan(long onTick, long offTick) {
		spans.add(new Span(onTick, offTick, this.duration));
	}


	// tick the timer
	@Override
	public void onTick() {
		if(!running) return;
		currentTick++;
		if(currentTick >= duration) currentTick = 0;
		// check state
		if(!updateState(testSpans())) return;
		if(deviceState)	gcTimer.log.info("Tick "+Long.toString(currentTick)+" "+title+" ON");
		else			gcTimer.log.info("Tick "+Long.toString(currentTick)+" "+title+" off");
	}


	// test spans
	public boolean testSpans() {
		for(Span span : spans)
			if(testSpan(span))
				return true;
		return false;
	}
	public boolean testSpan(Span span) {
		if(span.onTick < span.offTick)
			return (currentTick >= span.onTick && currentTick < span.offTick);
		else if(span.onTick > span.offTick)
			return (currentTick >= span.onTick || currentTick < span.offTick);
		return (currentTick == span.onTick);
	}


}
