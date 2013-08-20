package com.growcontrol.gctimer.timers;

import com.growcontrol.gcServer.devices.gcServerDeviceBoolean;
import com.growcontrol.gctimer.gcTimer;


public class timerClock extends gcServerDeviceBoolean implements deviceTimer {


	public timerClock(String name, String title) {
		super(name);
	}
	@Override
	public gcTimer.Type getTimerType() {
		return gcTimer.Type.CLOCK;
	}


	@Override
	public void setDuration(String duration) {
	}


	// tick the timer
	@Override
	public void onTick() {
	}


}
