package com.growcontrol.gctimer.timers;

import com.growcontrol.gcServer.devices.gcServerDeviceBoolean;
import com.growcontrol.gctimer.gcTimer;


public class timerSequencer extends gcServerDeviceBoolean implements deviceTimer {


	public timerSequencer(String name, String title) {
		super(name);
	}
	@Override
	public gcTimer.Type getTimerType() {
		return gcTimer.Type.SEQUENCER;
	}


	@Override
	public void setDuration(String duration) {
	}


	// tick the timer
	@Override
	public void onTick() {
	}


}
