package com.growcontrol.gctimer.timers;

import java.util.ArrayList;
import java.util.List;

import com.growcontrol.gcServer.gcServer;
import com.growcontrol.gcServer.devices.gcServerDeviceBoolean;
import com.growcontrol.gctimer.gcTimer;
import com.growcontrol.gctimer.gcTimer.TimerType;

public class timerTicker extends gcServerDeviceBoolean implements deviceTimer {

	private long duration = 0;
	private long currentTick = 0;

	private List<Span> spans = new ArrayList<Span>();


	// ticker span class
	public class Span {
		public long onTick;
		public long offTick;
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


	public timerTicker(String deviceName) {
		super(deviceName);
//TODO: load device configs
duration = 10;
spans.add(new Span(2, 5, duration));
	}
	@Override
	public TimerType getTimerType() {
		return TimerType.TICKER;
	}


	// tick the timer
	@Override
	public void onTick() {
		if(!running) return;
		currentTick++;
		if(currentTick >= duration) currentTick = 0;
		// check state
		if(updateState(testSpans()))
			if(deviceState)
				gcTimer.log.severe("Tick "+Long.toString(currentTick)+" DEVICE ON");
			else
				gcTimer.log.severe("Tick "+Long.toString(currentTick)+" DEVICE OFF");
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
