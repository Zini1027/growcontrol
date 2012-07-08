package com.growcontrol.gctimer.timers;

import com.growcontrol.gcServer.devices.gcServerDeviceBoolean;
import com.growcontrol.gctimer.gcTimer.TimerType;

public class timerClock extends gcServerDeviceBoolean implements deviceTimer {


	public timerClock(String deviceName) {
		super(deviceName);
	}
	@Override
	public TimerType getTimerType() {
		return TimerType.CLOCK;
	}


	// tick the timer
	@Override
	public void onTick() {
	}


}
