package com.growcontrol.gctimer.timers;

import com.growcontrol.gcServer.devices.gcServerDeviceBoolean;
import com.growcontrol.gctimer.gcTimer.TimerType;

public class timerSequencer extends gcServerDeviceBoolean implements deviceTimer {


	public timerSequencer(String deviceName) {
		super(deviceName);
	}
	@Override
	public TimerType getTimerType() {
		return TimerType.SEQUENCER;
	}


	// tick the timer
	@Override
	public void onTick() {
	}


}
